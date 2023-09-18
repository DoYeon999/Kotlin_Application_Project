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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Thread.sleep

class IntroActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
//        CoroutineScope(Dispatchers.Main).launch {
//            Log.d("application111", "called")
            Toast.makeText(this@IntroActivity, "날씨 정보를 받아오는 동안 날씨 기능이 작동하지 않을 수 있습니다.", Toast.LENGTH_LONG).show()
//            Log.d("application111", "called2")
//            Handler().postDelayed(Runnable {
//                val intent = Intent(this@IntroActivity, MainActivity::class.java)
//                Log.d("application111", "intro start")
//                finish()
//                startActivity(intent) }, 3000)
//            delay(3500L)
        //serviceStart()
        //}

        Handler().postDelayed(Runnable {
            val intent = Intent(this@IntroActivity, MainActivity::class.java)
            Log.d("application111", "intro start")
            startActivity(intent)
            finish() }, 3000L)

//        val intent = Intent(this@IntroActivity, MainActivity::class.java)
//        Log.d("application111", "called3")
//        finish()
//        Log.d("application111", "called4")
//        startActivity(intent)
//        Log.d("application111", "called5")
    }

    fun serviceStart()
    {
        val dbHelper = CustomDatabaseHelper(this@IntroActivity)
        Log.d("sqlite", "${dbHelper.databaseName}")
        val fishService = (applicationContext as APIApplication).fishService
        val obscodelist = listOf("DT_0054", "DT_0004", "DT_0005", "DT_0007", "DT_0012", "DT_0016", "DT_0027", "DT_0028", "DT_0029", "DT_0031", "DT_0035", "DT_0050", "DT_0056")
        dbHelper.clearTide()
        dbHelper.clearFirstDayWeather()
        dbHelper.clearOtherDayWeather()
        var i = 0
        obscodelist.forEach {
            val totalWeather = fishService.getTotalWeatherForecast(it)
            totalWeather.enqueue(object :  Callback<TotalWeatherDB> {
                override fun onResponse(
                    call: Call<TotalWeatherDB>,
                    response: Response<TotalWeatherDB>
                ) {
                    val nowobs = it
                    val tide = response.body()!!.todaytidelist
                    tide!!.forEach {
                        dbHelper.insertTide(it.obscode, it.tidelevel)
                    }

                    val firstday = response.body()!!.firstDayWeather
                    dbHelper.insertFirstDayWeather(
                        firstday.nowtemp, nowobs, firstday.tidetypeone, firstday.tidetimeone, firstday.tidelevelone,
                        firstday.tidetypetwo, firstday.tidetimetwo, firstday.tideleveltwo,
                        firstday.tidetypethree, firstday.tidetimethree, firstday.tidelevelthree,
                        firstday.tidetypefour, firstday.tidetimefour, firstday.tidelevelfour
                    )

                    val otherday = response.body()!!.otherDayWeather
                    var idx = 1
                    otherday.forEach {
                        Log.d("application111", "****${it.tidetypefour}****")
                        dbHelper.insertOtherDayWeather(
                            idx, it.mintemp, it.maxtemp, nowobs,
                            it.tidetypeone, it.tidetimeone, it.tidelevelone,
                            it.tidetypetwo, it.tidetimetwo, it.tideleveltwo,
                            it.tidetypethree, it.tidetimethree, it.tidelevelthree,
                            it.tidetypefour, it.tidetimethree, it.tidelevelfour
                        )
                        idx++
                    }

                    Log.d("application111", "success $i")
                    i++
                }
                override fun onFailure(call: Call<TotalWeatherDB>, t: Throwable) {
                    Log.d("SQLite Test", "failed")
                }
            })
        }
    }

    fun serviceStop()
    {
        val intent = Intent(this, WeatherService::class.java)
        stopService(intent)
    }

}