package com.example.mina

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //this is for hiding status bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN ,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed(
            {
                val intent = Intent(this,WebVieWer::class.java)
                startActivity(intent)
                finish()
            }
            ,
            3000
        )
    }
}
