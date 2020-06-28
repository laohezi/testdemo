package com.example.bigimageviewsample

import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.mcl.big_image_view.R

class MainActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }


}

class  Model{

    val mMediaStore = MediaStore()

    val imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI


    fun getAllImages(){

        val projection = arrayOf(MediaStore.Images.Media.DATA)

    }



}