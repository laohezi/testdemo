package com.yunzhi.plugin_p

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.yunzhi.pluginframe.BaseActivity
import dalvik.system.DexClassLoader
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bt_next.setOnClickListener {
            val intent = Intent(that, MainActivity2::class.java)
            startActivity(intent)
        }
    }
}


object Hookmanager {
    fun loadPlugin(activity: Activity) {
        val apkPath = Environment.getExternalStorageDirectory().path + "/pl.apk"
        val classLoader = DexClassLoader(apkPath,
            activity.getDir("dex",Context.MODE_PRIVATE).absolutePath,null,activity.classLoader
            )

        val packageManager = activity.packageManager
        val packageInfo = packageManager.getPackageArchiveInfo(apkPath,PackageManager.GET_ACTIVITIES)
        val aseetManagerClass = AssetManager::class.java
        try {
            val assertManagerObj = aseetManagerClass.newInstance()
         //   val

        }catch (e:Exception){

        }




    }


}
