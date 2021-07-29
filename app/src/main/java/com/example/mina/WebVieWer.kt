package com.example.mina

import android.annotation.TargetApi
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.Toast
import androidx.webkit.WebViewCompat


class WebVieWer : AppCompatActivity() {

    lateinit var webview : WebView

    private var uploadMessage: ValueCallback<Uri>? = null
    private var uploadMessageAboveL: ValueCallback<Array<Uri>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_viewer)

        //this is for hiding status bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN ,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //Connectivity varaiable
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        //Connectivity MANAGER
        if (connectivityManager != null)
        {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

            if (capabilities != null)
            {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    Toast.makeText( this, "Connected" , Toast.LENGTH_SHORT).show()
                    //return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    Toast.makeText( this, "Connected" , Toast.LENGTH_SHORT).show()
                    //return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    Toast.makeText( this, "Connected" , Toast.LENGTH_SHORT).show()
                    //return true
                }

                //WebView Call
                webview = findViewById<WebView>(R.id.webview)

                //Default Settings
                webview.apply {
                    //Configure related browser settings
                    this.settings.loadsImagesAutomatically = true
                    this.settings.useWideViewPort = true
                    this.settings.loadWithOverviewMode = true
                    this.settings.javaScriptEnabled = true
                    this.settings.allowContentAccess = true
                    this.settings.allowFileAccess = true
                    webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

                    //Handle url navigation page
                    webview.webViewClient = object : WebViewClient() {

                        val progressDialog = ProgressDialog(context)

                        override fun onPageStarted(view: WebView? , url: String? , favicon: Bitmap?) {
                            super.onPageStarted(view , url , favicon)
                            progressDialog.setTitle("Loading...")
                            progressDialog.setMessage("Please Wait!")
                            //progressDialog.
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
                            progressDialog.dismiss()
                            super.onReceivedError(view , request , error)
                            //val path: String = Uri.parse("file:///android_asset/404.html").toString()
                            //webview.loadUrl(path)
                            intentErr()
                        }

                        //Catch Redirections errors
                        override fun shouldOverrideUrlLoading(view: WebView? , url: String?): Boolean {
                            view?.loadUrl(url!!)
                            //Toast.makeText(this, url, Toast.LENGTH_SHORT).show()
                            return true
                        }
                    }
                }

                //WebChrome settings for mobile data access in file explorer
                webview.webChromeClient = object : WebChromeClient() {

                    // For Android < 3.0
                    fun openFileChooser(valueCallback: ValueCallback<Uri>) {
                        uploadMessage = valueCallback
                        openImageChooserActivity()
                    }

                    // For Android  >= 3.0
                    fun openFileChooser(valueCallback: ValueCallback<Uri>, acceptType: String) {
                        uploadMessage = valueCallback
                        openImageChooserActivity()
                    }

                    //For Android  >= 4.1
                    fun openFileChooser(
                        valueCallback: ValueCallback<Uri>,
                        acceptType: String,
                        capture: String
                    ) {
                        uploadMessage = valueCallback
                        openImageChooserActivity()
                    }

                    // For Android >= 5.0
                    override fun onShowFileChooser(
                        webView: WebView,
                        filePathCallback: ValueCallback<Array<Uri>>,
                        fileChooserParams: WebChromeClient.FileChooserParams
                    ): Boolean {
                        uploadMessageAboveL = filePathCallback
                        openImageChooserActivity()
                        return true
                    }
                }

                //Default link
                val targetUrl = "http://quick-computer.herokuapp.com/View/index.php"
                //val targetUrl = "http://mina.center/View/index.php"
                webview.loadUrl(targetUrl)

            }
            else
            {
                Toast.makeText( this, "Not connected" , Toast.LENGTH_SHORT).show()
                val intent = Intent(this,NoConnection::class.java)
                startActivity(intent)
                finish()
            }
        }
        else
        {
            Toast.makeText( this, "Not connected" , Toast.LENGTH_SHORT).show()
            val intent = Intent(this,NoConnection::class.java)
            startActivity(intent)
            finish()
        }

    }

    //ALL THE FUNCTIONS
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    private fun openImageChooserActivity() {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "image/*"
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) return
            val result = if (data == null || resultCode != Activity.RESULT_OK) null else data.data
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data)
            } else if (uploadMessage != null) {
                uploadMessage!!.onReceiveValue(result)
                uploadMessage = null
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun onActivityResultAboveL(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null)
            return
        var results: Array<Uri>? = null
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                val dataString = intent.dataString
                val clipData = intent.clipData
                if (clipData != null) {
                    results = Array(clipData.itemCount){
                            i -> clipData.getItemAt(i).uri
                    }
                }
                if (dataString != null)
                    results = arrayOf(Uri.parse(dataString))
            }
        }
        uploadMessageAboveL!!.onReceiveValue(results)
        uploadMessageAboveL = null
    }

    companion object {
        private val FILE_CHOOSER_RESULT_CODE = 10000
    }

    //Back hardware button
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        //If there is URL on history
        if(keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()){
            webview.goBack()
            return true
        }

        //Default back when there is no url
        return super.onKeyDown(keyCode, event)
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////




    //ALL FUNCTIONS IN TEST STATUS
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    fun intentErr(){
        val intent = Intent(this,NoConnection::class.java)
        startActivity(intent)
        finish()
    }

    private fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
}

