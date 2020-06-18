package com.mcl.big_image_view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.widget.ImageView

class MclImageView(context: Context, attrs: AttributeSet) :
    androidx.appcompat.widget.AppCompatImageView(context, attrs) {

    fun setImageBitmap(bmPath: String) {

    }

    fun loadInitial() {

    }


}

class MclImageViewModel {

    var config: Bitmap.Config = Bitmap.Config.ARGB_8888

    fun loadInitial(bmPath: String, width: Int, height: Int) {

      //  return BitmapFactory.

    }

    fun slice() {

    }


}