package com.yunzhi.testdemo

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        ARouter.openLog()
        ARouter.openDebug()
        ARouter.init(this)
    }

}