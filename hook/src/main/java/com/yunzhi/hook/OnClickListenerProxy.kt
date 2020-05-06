package com.yunzhi.hook

import android.util.Log
import android.view.View
import android.widget.Toast

class OnClickListenerProxy(val listener: View.OnClickListener) : View.OnClickListener {

    override fun onClick(v: View?) {
        Log.i("hooook", "点击我了")
        listener.onClick(v)
    }
}