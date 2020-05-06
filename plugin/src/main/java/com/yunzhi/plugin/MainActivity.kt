package com.yunzhi.plugin

import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bt_test.setOnClickListener {

            val intent = Intent()

            val compent = ComponentName(packageName, "com.yunzhi.plugin.Activity2")
            intent.setComponent(compent)
            startActivity(intent)

        }
    }
}
