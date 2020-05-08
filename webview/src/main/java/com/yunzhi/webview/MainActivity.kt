package com.yunzhi.webview

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val webSettings = web_view.settings

        webSettings.javaScriptEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true


        bt.setOnClickListener {

            web_view.post {
                web_view.loadUrl("javascript:callJs()")
            }

        }

        web_view.addJavascriptInterface(JsInterface(),"test")

        web_view.loadUrl("file:///android_asset/index.html#/user")

        web_view.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                val title = view.title
                supportActionBar?.title = title

            }


        }


    }
}

class JsInterface:Any() {
    @JavascriptInterface
    fun hello(msg: String) {
        Log.d("jscall", "js 呼叫了我,消息是:${msg}")

    }


}
