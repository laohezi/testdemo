package com.yunzhi.testdemo

import android.app.Application

class MyApp :Application(){

    override fun onCreate() {
        super.onCreate()
        Thread.sleep(3000)

    }

}