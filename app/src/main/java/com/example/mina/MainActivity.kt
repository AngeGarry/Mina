package com.example.mina

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {

        //this is for hiding status bar
        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN ,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
