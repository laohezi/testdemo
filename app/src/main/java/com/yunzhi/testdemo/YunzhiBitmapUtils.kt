package com.yunzhi.testdemo

import android.content.Context
import android.graphics.*
import android.text.*
import android.util.*

import java.io.*
import java.lang.IllegalArgumentException
import android.graphics.Bitmap
import android.renderscript.*


import android.os.Build
import android.view.View



class YunzhiBitmapUtils {
    companion object {

        private var instance: YunzhiBitmapUtils? = null
        fun getInstance(context: Context): YunzhiBitmapUtils {

            if (instance != null) {
                return instance!!
            }
            synchronized(YunzhiBitmapUtils::class.java) {
                if (instance != null) {
                    return instance!!
                }
                instance = YunzhiBitmapUtils()
                instance!!.nV21ToBitmap = NV21ToBitmap.getInstance(context)
                return instance!!

            }
        }

    }

    var rgba: IntArray? = null
    lateinit var nV21ToBitmap: NV21ToBitmap
    @Throws(IllegalArgumentException::class)
    fun yuvToRGB(data: ByteArray, width: Int, height: Int): IntArray? {
        return nv21ToRGB(data, width, height)
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

    fun bitmapTobytes(bitmap: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }


    fun bitMapToString(bitmap: Bitmap): String {
        return Base64.encodeToString(bitmapTobytes(bitmap), Base64.DEFAULT)
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
        return BitmapFactory.decodeByteArray(byte, 0, byte.size)
    }


    fun bitmapToBase64(bitmap: Bitmap?): String? {

        // 要返回的字符串
        var reslut: String? = null

        var baos: ByteArrayOutputStream? = null

        try {

            if (bitmap != null) {

                baos = ByteArrayOutputStream()
                /**
                 * 压缩只对保存有效果bitmap还是原来的大小
                 */
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos)

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
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos)

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
    fun saveFile(bm: Bitmap, fileName: String, dir: String): String {
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
        bm.compress(Bitmap.CompressFormat.JPEG, 85, bos)
        bos.flush()
        bos.close()
        return dir + fileName
    }




    fun nv21ToBitMap(nv21: ByteArray?, width: Int, height: Int): Bitmap? {
        if (nv21 == null || nv21.isEmpty()) {
            return null
        }
        return nV21ToBitmap.nv21ToBitmap(nv21, width, height)
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


class NV21ToBitmap(var context: Context) {


    companion object {
        private var instance: NV21ToBitmap? = null
        fun getInstance(context: Context): NV21ToBitmap {
            if (instance != null) {
                return instance!!
            }
            synchronized(NV21ToBitmap::class.java) {
                if (instance != null) {
                    return instance!!
                }
                instance = NV21ToBitmap(context)
                instance!!.init()
                return instance!!
            }
        }
    }

    private var rs: RenderScript? = null
    private var yuvToRgbIntrinsic: ScriptIntrinsicYuvToRGB? = null
    private var yuvType: Type.Builder? = null
    private var rgbaType: Type.Builder? = null
    private var `in`: Allocation? = null
    private var out: Allocation? = null

    fun init() {
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

        yuvToRgbIntrinsic!!.setInput(`in`)
        yuvToRgbIntrinsic!!.forEach(out)
        val pixes = IntArray(width * height)
        out!!.copyTo(pixes)
        return pixes

    }


    fun nv21ToBitmap(nv21: ByteArray, width: Int, height: Int): Bitmap? {
        try {
            if (yuvType == null) {
                yuvType = Type.Builder(rs, Element.U8(rs)).setX(nv21.size)
                `in` = Allocation.createTyped(rs, yuvType!!.create(), Allocation.USAGE_SCRIPT)

                rgbaType = Type.Builder(rs, Element.RGBA_8888(rs)).setX(width).setY(height)
                out = Allocation.createTyped(rs, rgbaType!!.create(), Allocation.USAGE_SCRIPT)
            }

            `in`!!.copyFrom(nv21)

            yuvToRgbIntrinsic?.setInput(`in`)
            yuvToRgbIntrinsic?.forEach(out)
            val bmpout = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            out!!.copyTo(bmpout)
            return bmpout
        }catch (e:java.lang.Exception){
            e.printStackTrace()
            return null
        }


    }

}

