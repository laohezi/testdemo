package com.yunzhi.base.utils

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.renderscript.*
import android.text.TextUtils
import android.util.Base64
import android.util.LruCache
import android.view.View
import com.blankj.utilcode.util.FileUtils
import com.yunzhi.base.BaseApp
import java.io.*
import java.lang.ref.WeakReference
import java.util.*


object BitmapUtils {

    val bitmapPool = BitmapPool().apply { init(BaseApp.getContext()) }
    var rgba: IntArray? = null
    private val nV21ToBitmap = NV21ToBitmap(BaseApp.getContext())

    @Throws(IllegalArgumentException::class)
    fun yuvToRGB(data: ByteArray, width: Int, height: Int): IntArray {
        val frameSize = width * height
        rgba = IntArray(frameSize)
        for (i in 0 until height)
            for (j in 0 until width) {
                var y = 0xff and data[i * width + j].toInt()
                val u = 0xff and data[frameSize + (i shr 1) * width + (j and 1.inv()) + 0].toInt()
                val v = 0xff and data[frameSize + (i shr 1) * width + (j and 1.inv()) + 1].toInt()
                y = if (y < 16) 16 else y
                var r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128))
                var g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128))
                var b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128))
                r = if (r < 0) 0 else if (r > 255) 255 else r
                g = if (g < 0) 0 else if (g > 255) 255 else g
                b = if (b < 0) 0 else if (b > 255) 255 else b
                rgba!![i * width + j] = -0x1000000 + (b shl 16) + (g shl 8) + r
            }

        return rgba!!
    }

    fun getBitmapFromPath(path: String): Bitmap? {
        if (TextUtils.isEmpty(path)) {
            return null
        }

        if (!File(path).exists()) {
            System.err.println("getBitmapFromPath: file not exists")
            return null
        }

        val buf = ByteArray(1024 * 1024)// 1M
        var bitmap: Bitmap? = null

        try {

            val fis = FileInputStream(path)
            val len = fis.read(buf, 0, buf.size)
            bitmap = BitmapFactory.decodeByteArray(buf, 0, len)
            if (bitmap == null) {
                println("len= $len")
                System.err.println("path: $path  could not be decode!!!")
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }

        return bitmap
    }


    fun copyBitmap(bitmap: Bitmap): Bitmap {
        return Bitmap.createBitmap(bitmap)
    }

    fun bitmapTobytes(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int): ByteArray {
        val baos = ByteArrayOutputStream()
        bitmap.compress(format, quality, baos)
        return baos.toByteArray()
    }


    fun bitMapToString(
        bitmap: Bitmap,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        quality: Int = 70
    ): String {
        return Base64.encodeToString(bitmapTobytes(bitmap, format, quality), Base64.DEFAULT)
    }

    fun bitMapToString(
        bitmapPath: String,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        quality: Int = 70
    ): String? {
        getBitmapFromPath(bitmapPath)?.let {
            return bitMapToString(it, format, quality)
        }
        return null
    }


    private fun convertYUVtoRGB(y: Int, u: Int, v: Int): Int {
        var r: Int
        var g: Int
        var b: Int
        r = y + (1.402f * v).toInt()
        g = y - (0.344f * u + 0.714f * v).toInt()
        b = y + (1.772f * u).toInt()
        r = if (r > 255) 255 else if (r < 0) 0 else r
        g = if (g > 255) 255 else if (g < 0) 0 else g
        b = if (b > 255) 255 else if (b < 0) 0 else b
        return -0x1000000 or (b shl 16) or (g shl 8) or r
    }


    fun base64ToBitmap(string: String?): Bitmap? {
        if (string == null) {
            return null
        }
        val byte = Base64.decode(string, Base64.DEFAULT)
        return try {
            BitmapFactory.decodeByteArray(byte, 0, byte.size)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }

    fun compressBySampleSize(
        src: Bitmap,
        maxWidth: Int,
        maxHeight: Int,
        recycle: Boolean,
        quality: Int = 80,
        compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    ): Bitmap? {

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        val baos = ByteArrayOutputStream()
        src.compress(compressFormat, quality, baos)
        val bytes = baos.toByteArray()
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
        options.inJustDecodeBounds = false
        if (recycle && !src.isRecycled) src.recycle()
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
    }

    fun compressBySampleSize(
        src: Bitmap,
        sampleSize: Int,
        recycle: Boolean = false,
        quality: Int = 80,
        compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    ): Bitmap? {
        val options = BitmapFactory.Options()
        options.inSampleSize = sampleSize
        val baos = ByteArrayOutputStream()
        src.compress(compressFormat, quality, baos)
        val bytes = baos.toByteArray()
        if (recycle && !src.isRecycled) src.recycle()
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        maxWidth: Int,
        maxHeight: Int
    ): Int {
        var height = options.outHeight
        var width = options.outWidth
        var inSampleSize = 1
        while (height > maxHeight || width > maxWidth) {
            height = height shr 1
            width = width shr 1
            inSampleSize = inSampleSize shl 1
        }
        return inSampleSize
    }


    fun bitmapToBase64(
        bitmap: Bitmap?,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        quality: Int = 75
    ): String? {

        // 要返回的字符串
        var reslut: String? = null

        var baos: ByteArrayOutputStream? = null

        try {

            if (bitmap != null) {

                baos = ByteArrayOutputStream()
                /**
                 * 压缩只对保存有效果bitmap还是原来的大小
                 */
                bitmap.compress(format, quality, baos)

                baos.flush()
                baos.close()
                // 转换为字节数组
                val byteArray = baos.toByteArray()

                // 转换为字符串
                reslut = Base64.encodeToString(byteArray, Base64.DEFAULT)
            } else {
                return null
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {

            try {

                baos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return reslut

    }

    fun bitmapToBase64WithoutWrap(bitmap: Bitmap?): String? {

        // 要返回的字符串
        var reslut: String? = null

        var baos: ByteArrayOutputStream? = null

        try {

            if (bitmap != null) {

                baos = ByteArrayOutputStream()
                /**
                 * 压缩只对保存有效果bitmap还是原来的大小
                 */
                bitmap.compress(Bitmap.CompressFormat.PNG, 30, baos)

                baos.flush()
                baos.close()
                // 转换为字节数组
                val byteArray = baos.toByteArray()

                // 转换为字符串
                reslut = Base64.encodeToString(byteArray, Base64.NO_WRAP)
            } else {
                return null
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {

            try {
                baos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return reslut
    }

    @Deprecated("fun name not clear change to getRGB(bitmap)")
    fun getPixels(bitmap: Bitmap?): IntArray? {
        bitmap?.let {
            val width = bitmap.width
            val height = bitmap.height
            val pixels = IntArray(width * height)
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
            return pixels
        }
        return null
    }

    fun getRgb(bitmap: Bitmap?): IntArray? {
        return getPixels(bitmap)
    }


    @Throws(IOException::class)
    fun saveFile(
        bm: Bitmap,
        fileName: String,
        dir: String,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        quality: Int = 75
    ): String {
        val subForder = dir
        val foder = File(subForder)
        if (!foder.exists()) {
            foder.mkdirs()
        }
        val myCaptureFile = File(subForder, fileName)
        if (!myCaptureFile.exists()) {
            myCaptureFile.createNewFile()
        }
        val bos = BufferedOutputStream(FileOutputStream(myCaptureFile))
        bm.compress(format, quality, bos)
        bos.flush()
        bos.close()
        return dir + fileName
    }

    @Throws(IOException::class)
    fun saveFile(
        bm: Bitmap, fullPath: String, quality: Int = 70, compressFormat: Bitmap
        .CompressFormat = Bitmap.CompressFormat.JPEG, sampleSize: Int = 1
    ): String {
        FileUtils.createOrExistsDir(fullPath)
        val file = File(fullPath)
        val bos = BufferedOutputStream(FileOutputStream(file))
        bm.compress(compressFormat, quality, bos)
        bos.flush()
        bos.close()
        return fullPath
    }


    fun nv21ToBitMap(
        nv21: ByteArray?,
        width: Int,
        height: Int,
        config: Bitmap.Config = Bitmap.Config.RGB_565

    ): Bitmap? {
        if (nv21 == null || nv21.isEmpty()) {
            return null
        }

        val bitmap = bitmapPool.getBitmapFromPool(width, height, 1)


        nV21ToBitmap.nv21ToBitmap(
            nv21,
            width,
            height,
            bmpout = bitmap,
            config = config
        )
        return bitmap
    }


    fun nv21ToRGB(nv21: ByteArray?, width: Int, height: Int): IntArray? {
        if (nv21 == null) {
            return null
        }
        try {
            val bitmapVideo = nv21ToBitMap(nv21, width, height)
            return getPixels(bitmapVideo)
        } catch (e: Exception) {
            return null
        }
    }

    fun getViewBp(v: View): Bitmap {
        v.setDrawingCacheEnabled(true)
        v.buildDrawingCache()
        if (Build.VERSION.SDK_INT >= 11) {
            v.measure(
                View.MeasureSpec.makeMeasureSpec(
                    v.getWidth(),
                    View.MeasureSpec.EXACTLY
                ), View.MeasureSpec.makeMeasureSpec(
                    v.getHeight(), View.MeasureSpec.EXACTLY
                )
            )
            v.layout(
                v.getX() as Int, v.getY() as Int,
                v.getX() as Int + v.getMeasuredWidth(),
                v.getY() as Int + v.getMeasuredHeight()
            )
        } else {
            v.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight())
        }
        val b = Bitmap.createBitmap(
            v.getDrawingCache(),
            0,
            0,
            v.getMeasuredWidth(),
            v.getMeasuredHeight()
        )

        v.setDrawingCacheEnabled(false)
        v.destroyDrawingCache()
        return b
    }


}


class NV21ToBitmap(context: Context) {

    private val rs: RenderScript
    private val yuvToRgbIntrinsic: ScriptIntrinsicYuvToRGB
    private var yuvType: Type.Builder? = null
    private var rgbaType: Type.Builder? = null
    private var `in`: Allocation? = null
    private var out: Allocation? = null

    init {
        rs = RenderScript.create(context)
        yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs))
    }


    fun nv21ToRgb(nv21: ByteArray, width: Int, height: Int): IntArray {
        if (yuvType == null) {
            yuvType = Type.Builder(rs, Element.U8(rs)).setX(nv21.size)
            `in` = Allocation.createTyped(rs, yuvType!!.create(), Allocation.USAGE_SCRIPT)

            rgbaType = Type.Builder(rs, Element.RGBA_8888(rs)).setX(width).setY(height)
            out = Allocation.createTyped(rs, rgbaType!!.create(), Allocation.USAGE_SCRIPT)
        }

        `in`!!.copyFrom(nv21)

        yuvToRgbIntrinsic.setInput(`in`)
        yuvToRgbIntrinsic.forEach(out)


        val pixes = IntArray(width * height)
        out!!.copyTo(pixes)
        return pixes

    }


    fun nv21ToBitmap(
        nv21: ByteArray,
        width: Int,
        height: Int,
        bmpout: Bitmap,
        config: Bitmap.Config
    ) {
        if (yuvType == null) {
            yuvType = Type.Builder(rs, Element.U8(rs)).setX(nv21.size)
            `in` = Allocation.createTyped(rs, yuvType!!.create(), Allocation.USAGE_SCRIPT)
            when (config) {
                Bitmap.Config.ARGB_8888 -> {
                    rgbaType = Type.Builder(rs, Element.RGBA_8888(rs)).setX(width).setY(height)
                }
                Bitmap.Config.RGB_565 -> {
                    rgbaType = Type.Builder(rs, Element.RGB_565(rs)).setX(width).setY(height)
                }

            }

            out = Allocation.createTyped(rs, rgbaType!!.create(), Allocation.USAGE_SCRIPT)
        }

        `in`!!.copyFrom(nv21)

        yuvToRgbIntrinsic.setInput(`in`)
        yuvToRgbIntrinsic.forEach(out)
        val bmpout =
            out!!.copyTo(bmpout)
    }

}


class BitmapPool {
    lateinit var mCache: LruCache<String, Bitmap>

    lateinit var reusableCache: Set<WeakReference<Bitmap>>
    fun init(context: Context) {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        mCache = object : LruCache<String, Bitmap>(am.memoryClass * 1024 * 1024 / 8) {

            override fun sizeOf(key: String, value: Bitmap): Int {
                return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    value.allocationByteCount
                } else {
                    value.byteCount
                }
            }

            override fun entryRemoved(
                evicted: Boolean,
                key: String,
                oldValue: Bitmap,
                newValue: Bitmap
            ) {
                if (oldValue.isMutable) {
                    reusableCache.plus(WeakReference(oldValue))
                } else {
                    oldValue.recycle()
                }

            }
        }
        reusableCache = Collections.synchronizedSet(HashSet())
    }

    fun getBitmapFromPool(
        width: Int,
        height: Int,
        inSampleSize: Int,
        config: Bitmap.Config = Bitmap.Config.ARGB_8888
    ): Bitmap {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return Bitmap.createBitmap(width, height, config)
        }

        val iterator: MutableIterator<WeakReference<Bitmap>> =
            reusableCache.iterator() as MutableIterator<WeakReference<Bitmap>>

        while (iterator.hasNext()) {

            val bitmap = iterator.next().get()
            bitmap?.let {
                if (canUse(bitmap, width, height, inSampleSize)) {
                    iterator.remove()
                    return it
                }

            }
        }

        val bit = Bitmap.createBitmap(width, height, config)

        mCache.put(UUID.randomUUID().toString(), bit)
        return bit


    }

    private fun canUse(bitmap: Bitmap, width: Int, height: Int, inSampleSize: Int): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            bitmap.width == width && bitmap.height == height && inSampleSize == 1
        } else {
            val nW = width / inSampleSize
            val nH = height / inSampleSize
            val byteCount = nW * nH * getPixel(bitmap.config)
            byteCount < bitmap.byteCount
        }

    }

    private fun getPixel(config: Bitmap.Config): Int {
        return when (config) {
            Bitmap.Config.ARGB_8888 -> {
                4
            }
            else -> {
                2
            }


        }
    }


}

