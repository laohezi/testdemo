package com.yunzhi.base.compement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.yokeyword.fragmentation.SupportFragment

abstract class BaseFragment : SupportFragment() {

    abstract fun getLayoutId(): Int
    var mRoot: View? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRoot == null) {
            mRoot = inflater.inflate(getLayoutId(), null)
        } else {
            val parent = mRoot!!.parent as ViewGroup
            parent.removeView(mRoot)
        }

        return mRoot
    }


}