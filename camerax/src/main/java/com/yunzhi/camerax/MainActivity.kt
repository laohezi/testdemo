package com.yunzhi.camerax

import android.Manifest
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Rational
import android.util.Size
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import com.yunzhi.base.BaseApp
import com.yunzhi.base.utils.BitmapUtils
import com.yunzhi.base.utils.NV21ToBitmap

import kotlinx.android.synthetic.main.activity_main.*

import java.nio.ByteBuffer


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startCamera()

        setCamera()
    }

    private fun setCamera() {

    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun startCamera() {
        val previewConfig = PreviewConfig.Builder().apply {
            setTargetAspectRatio(Rational(1, 1))
            setTargetResolution(Size(640, 480))
        }
            .build()

        val preview = Preview(previewConfig)

        val analyzerConfig = ImageAnalysisConfig.Builder().apply {
            setTargetResolution(Size(1600, 1200))
            setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)


        }
            .build()

        val imageAnalyzer = ImageAnalysis(analyzerConfig).apply {
            analyzer = MyImageAnalyzer()

        }





        preview.setOnPreviewOutputUpdateListener {
            camera_view.surfaceTexture = it.surfaceTexture

        }

        CameraX.bindToLifecycle(this, preview, imageAnalyzer)


    }


}


class MyImageAnalyzer : ImageAnalysis.Analyzer {

    val nV21ToBitmap = NV21ToBitmap(BaseApp.getContext())
    override fun analyze(image: ImageProxy, rotationDegrees: Int) {
        val bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
        nV21ToBitmap.nv21ToBitmap(
            image.planes[0].buffer.toByteArray(),
            image.width, image.height,
            bitmap,
            Bitmap.Config.ARGB_8888
        )
        val config = bitmap.config


    }


    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()    // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data)   // Copy the buffer into a byte array
        return data // Return the byte array
    }

}
