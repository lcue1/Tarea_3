package com.example.juego_ppt

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    //Atrubutes
    lateinit var scoreGamer:TextView
    lateinit var scoreAndroid:TextView
    lateinit var gameImgSelection:ImageView
    lateinit var androidImgSelection:ImageView
    lateinit var btnStartStop:Button
    lateinit var btnReset:Button

    lateinit var handleButtons:HandleButtons

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initializeProperties()

    }


    private fun initializeProperties() {
        //Init views
        scoreGamer=findViewById(R.id.scoreGamer)
        scoreAndroid=findViewById(R.id.scoreAndroid)

        gameImgSelection=findViewById(R.id.gameImgSelection)
        androidImgSelection=findViewById(R.id.androidImgSelection)

        btnStartStop=findViewById(R.id.btnStartStop)
        btnReset=findViewById(R.id.btnReset)
        //Init Object that managger app
        handleButtons=HandleButtons(this,
            scoreGamer ,
            scoreAndroid,
            btnStartStop,btnReset,
            gameImgSelection,
            androidImgSelection)
    }

}