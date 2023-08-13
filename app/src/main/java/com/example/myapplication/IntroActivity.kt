package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.myapplication.kdy.FishinfoActivity
import com.example.myapplication.weather_imgfind.weather.MapActivity

class IntroActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        Handler().postDelayed(Runnable {
            val intent = Intent(this@IntroActivity, FishinfoActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}