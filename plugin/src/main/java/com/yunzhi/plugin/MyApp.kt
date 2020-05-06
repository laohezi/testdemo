package com.yunzhi.plugin

import android.app.Application
import java.lang.Appendable

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        HookHelper.hookClassLoader(this)
    }


}