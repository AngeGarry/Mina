package com.example.mina

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.webkit.WebViewCompat

class WebVieWer : AppCompatActivity() {

    lateinit var myWebView : WebView

    override fun onCreate(savedInstanceState: Bundle?) {

        //this is for hiding status bar
        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN ,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_viewer)

        myWebView = findViewById(R.id.webview)

        //Add JavaScript interface for popups
        //myWebView.addJavascriptInterface(WebAppInterface(this), "Android")

        //Enable JavaScript
        myWebView.settings.javaScriptEnabled = true

        //Handle page navigation
        myWebView.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url!!)
                return true
            }
        }

        //Add default URL
        myWebView.loadUrl("https://quick-computer.herokuapp.com/")

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        //If there is URL on history
        if(keyCode == KeyEvent.KEYCODE_BACK && myWebView.canGoBack()){
            myWebView.goBack()
            return true
        }

        //Default back when there is no url
        return super.onKeyDown(keyCode, event)
    }
}


//Instantiate the interface and set the context
class WebAppInterface(private val mContext: Context){

    //Show a toast
    @JavascriptInterface
    fun showToast(toast: String){
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
    }
}