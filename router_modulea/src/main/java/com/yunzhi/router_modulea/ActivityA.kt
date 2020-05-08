package com.yunzhi.router_modulea

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.activitya.*

@Route(path = "/moduleA/activityA")
class  ActivityA :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitya)
        bt_jump.setOnClickListener {

            ARouter.getInstance().build("/moduleB/activityB").navigation()
        }
    }

}