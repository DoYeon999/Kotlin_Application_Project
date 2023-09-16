package com.example.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.myapplication.kdy.FishinfoActivity
import com.example.myapplication.kdy.LoginActivity
import com.example.myapplication.sqliteProcess.CustomDatabaseHelper
import com.example.myapplication.sqliteProcess.WeatherService
import com.example.myapplication.weather_imgfind.model.TotalWeatherDB
import com.example.myapplication.weather_imgfind.net.APIApplication
import com.example.myapplication.weather_imgfind.weather.MapActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IntroActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        serviceStart()
        setContentView(R.layout.activity_intro)
        Toast.makeText(this@IntroActivity, "날씨 정보를 받아오는 동안 날씨 기능이 작동하지 않을 수 있습니다.", Toast.LENGTH_LONG).show()
         Handler().postDelayed(Runnable {
               val intent = Intent(this@IntroActivity, MainActivity::class.java)
               finish()
               startActivity(intent) }, 3000)
    }

    fun serviceStart()
    {
        Log.d("google1234", "started!!")
        val intent = Intent(this, WeatherService::class.java)
        startService(intent)
    }

    fun serviceStop()
    {
        val intent = Intent(this, WeatherService::class.java)
        stopService(intent)
    }

}