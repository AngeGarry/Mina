package com.example.mina

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi

class NoConnection : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_connection)

        //this is for hiding status bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN ,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


    }

    //Check for connection
    @RequiresApi(Build.VERSION_CODES.M)
    fun onTouch(view: View) {
        //Connectivity varaiable
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (connectivityManager != null) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet" , "NetworkCapabilities.TRANSPORT_CELLULAR")
                    Toast.makeText(this , "Connected" , Toast.LENGTH_SHORT).show()
                    //return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet" , "NetworkCapabilities.TRANSPORT_WIFI")
                    Toast.makeText(this , "Connected" , Toast.LENGTH_SHORT).show()
                    //return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet" , "NetworkCapabilities.TRANSPORT_ETHERNET")
                    Toast.makeText(this , "Connected" , Toast.LENGTH_SHORT).show()
                    //return true
                }

                val intent = Intent(this , WebVieWer::class.java)
                startActivity(intent)
                finish()

            } else {
                Toast.makeText(this , "Not connected" , Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this , "Not connected" , Toast.LENGTH_SHORT).show()
        }
    }
}