package com.mcl.big_image_view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView

class MclImageView(context: Context, attrs: AttributeSet) :
    androidx.appcompat.widget.AppCompatImageView(context, attrs) {
    val model = MclModel()

    fun setImageBitmap(bmPath: String) {
        loadInitial(bmPath)

    }

    fun loadInitial(bmPath: String) {
        setImageBitmap(model.loadInitial(bmPath, width, height))
    }


}

class MclModel {

    var config: Bitmap.Config = Bitmap.Config.ARGB_8888

    fun loadInitial(bmPath: String, viewWidth: Int, viewHeight: Int): Bitmap? {

        val options = BitmapFactory.Options()
        options.apply {
            inJustDecodeBounds = true
        }
        val bitmap = BitmapFactory.decodeFile(bmPath, options)
        val bmWidth = bitmap.width
        val bmHeight = bitmap.height
        options.inJustDecodeBounds = false
        calculateSample(bmWidth, bmHeight, viewWidth, viewHeight, options)
        return BitmapFactory.decodeFile(bmPath, options)

    }

    fun slice() {

    }

    fun calculateSample(
        bmpWidth: Int,
        bmpHeight: Int,
        viewWidth: Int,
        viewHeight: Int,
        options: BitmapFactory.Options
    ) {
        options.inSampleSize = 1

    }


}