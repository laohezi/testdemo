package com.yunzhi.base

import android.app.Application

open class BaseApp :Application(){


    companion object{
        private lateinit  var context :Application
       fun  getContext(): Application {
           return context
        }


    }

    override fun onCreate() {
        super.onCreate()
        context = this

    }



}