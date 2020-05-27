package com.yunzhi.webview

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val webSettings = web_view.settings

        webSettings.javaScriptEnabled = true
        // webSettings.javaScriptCanOpenWindowsAutomatically = true


        bt.setOnClickListener {
            web_view.post {
                var lala = "lalalalalalalalalalaalal"
                web_view.evaluateJavascript("javascript:callJs('${lala}')",object :ValueCallback<String>{
                    override fun onReceiveValue(value: String?) {

                    }

                })
            }

        }


        web_view.webChromeClient = object : WebChromeClient() {

            override fun onJsAlert(
                view: WebView?,
                url: String?,
                message: String,
                result: JsResult?
            ): Boolean {
                ToastUtils.showShort(message)
                return false
            }


        }

        web_view.addJavascriptInterface(JsInterface(), "test")
        web_view.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                val title = view.title
                supportActionBar?.title = title
            }


        }

        web_view.loadUrl("file:///android_asset/test.html")


    }
}

class JsInterface : Any() {
    @JavascriptInterface

    fun hello(msg: String): String {
        Log.d("jscall", "js 呼叫了我,消息是:${msg}")
        return "我是来自Android 的 text"

    }


}
