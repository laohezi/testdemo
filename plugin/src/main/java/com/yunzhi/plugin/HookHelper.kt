package com.yunzhi.plugin

import android.app.Application
import android.content.Context
import android.util.Log
import java.lang.reflect.Field

object HookHelper {
    fun hookClassLoader(context: Application) {
        val mLoadedApk = getFieldValue(context::class.java.superclass!!, context, "mLoadedApk")!!
        val classLoader =
            getFieldValue(mLoadedApk::class.java, mLoadedApk, "mClassLoader")!! as ClassLoader
        val classLoaderFiled = getFiled(mLoadedApk::class.java, "mClassLoader")
        classLoaderFiled.set(mLoadedApk, object : ClassLoader() {

            override fun loadClass(name: String): Class<*> {
                if (name.contains("Activity2")) {
                    Log.d("66666666", "name =${name}")
                    val newName = name.replace("Activity2", "Activity3")
                    Log.d("66666666", "替换后name =${name}")
                    return classLoader.loadClass(newName)
                }
                return classLoader.loadClass(name)
            }

        })

    }


    fun getFiled(clz: Class<in Nothing>, filedName: String): Field {
        val fiele = clz.getDeclaredField(filedName)
        if (fiele != null) {
            fiele.isAccessible = true
        }
        return fiele
    }

    fun getFieldValue(clz: Class<in Nothing>, o: Any, filedName: String): Any? {
        val filed = getFiled(clz, filedName)
        return filed.get(o)
    }


}