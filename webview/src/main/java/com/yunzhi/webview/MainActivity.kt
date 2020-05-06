package com.yunzhi.webview

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
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

        web_view.loadUrl("file:///android_asset/test.html")

        web_view.webChromeClient = object : WebChromeClient() {

            override fun onJsAlert(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {

                AlertDialog.Builder(this@MainActivity)
                    .setTitle("我来自js")
                    .setMessage(message)
                    .create()
                    .show()

                return true

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
