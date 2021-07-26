package com.example.mina

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.Toast
import androidx.webkit.WebViewCompat

class WebVieWer : AppCompatActivity() {

    lateinit var myWebView : WebView
    lateinit var context : Context
    lateinit var upLoadListener: DownloadListener
    var readAccess = false

    override fun onCreate(savedInstanceState: Bundle?) {

        //this is for hiding status bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN ,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_viewer)

        //find layout
        myWebView = findViewById(R.id.webview)

        // Enable responsive layout
        myWebView.settings.useWideViewPort = true

        // Zoom out if the content width is greater than the width of the viewport
        myWebView.settings.loadWithOverviewMode = true

        //Add JavaScript interface for popups
        //myWebView.addJavascriptInterface(WebAppInterface(this), "Android")

        /*myWebView.webChromeClient = object : WebChromeClient(){

        }*/

        //Default Settings
        myWebView.apply {
            //Configure related browser settings
            this.settings.loadsImagesAutomatically = true
            this.settings.javaScriptEnabled = true
            this.settings.allowContentAccess = true
            this.settings.allowFileAccess = true
            myWebView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

            //Handle url navigation page
            myWebView.webViewClient = object : WebViewClient() {

                val progressDialog = ProgressDialog(context)

                override fun onPageStarted(view: WebView? , url: String? , favicon: Bitmap?) {
                    super.onPageStarted(view , url , favicon)
                    progressDialog.setTitle("Loading...")
                    progressDialog.setMessage("Please Wait!")
                    progressDialog.setCancelable(false)
                    progressDialog.show()
                }

                override fun onPageCommitVisible(view: WebView? , url: String?) {
                    super.onPageCommitVisible(view , url)
                    if (progressDialog != null) {
                        progressDialog.dismiss()
                    }
                }

                override fun onReceivedError(
                    view: WebView? ,
                    request: WebResourceRequest? ,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view , request , error)
                    val path: String = Uri.parse("file:///android_asset/404.html").toString()
                    myWebView.loadUrl(path)

                }

                override fun shouldOverrideUrlLoading(view: WebView? , url: String?): Boolean {
                    view?.loadUrl(url!!)
                    //Toast.makeText(this, url, Toast.LENGTH_SHORT).show()
                    return true
                }
            }

            //Add default URL
            myWebView.loadUrl("https://quick-computer.herokuapp.com/")

        }

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