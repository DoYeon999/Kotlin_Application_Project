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
import java.util.concurrent.Executors

class IntroActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
//        CoroutineScope(Dispatchers.Main).launch {
//            Log.d("application111", "called")
//            Log.d("application111", "called2")
//            Handler().postDelayed(Runnable {
//                val intent = Intent(this@IntroActivity, MainActivity::class.java)
//                Log.d("application111", "intro start")
//                finish()
//                startActivity(intent) }, 3000)
//            delay(3500L)
        //serviceStart()
        //}

        val executor = Executors.newSingleThreadExecutor()
        val dbHelper = CustomDatabaseHelper(this@IntroActivity)
        Log.d("application111", "${dbHelper.databaseName}")
        val fishService = (applicationContext as APIApplication).fishService
        val obscodelist = listOf(
            "DT_0054",
            "DT_0004",
            "DT_0005",
            "DT_0007",
            "DT_0012",
            "DT_0016",
            "DT_0027",
            "DT_0028",
            "DT_0029",
            "DT_0031",
            "DT_0035",
            "DT_0050",
            "DT_0056"
        )
        dbHelper.clearTide()
        dbHelper.clearFirstDayWeather()
        dbHelper.clearOtherDayWeather()
        var i = 0

        val totalWeather = fishService.getTotalWeatherForecast()
        totalWeather.enqueue(object : Callback<List<TotalWeatherDB>> {
            override fun onResponse(
                call: Call<List<TotalWeatherDB>>,
                response: Response<List<TotalWeatherDB>>
            ) {
                Log.d("application111", "${response.body()!!}")
                val sharedPref = getSharedPreferences("logininfo", MODE_PRIVATE)
                executor.execute {
                    sharedPref.edit().run{
                        putBoolean("setdatabase", false)
                    }.commit()
                    Log.d("application111", "asdasdgh222222")
                    val totaldata = response.body()!!
                    totaldata.forEach {nowdata ->
                        val nowobs =  obscodelist.get(i)

                        val tide = nowdata.todaytidelist

                        tide!!.forEach {
                            dbHelper.insertTide(it.obscode, it.tidelevel)
                        }

                        val firstday = nowdata.firstDayWeather
                        dbHelper.insertFirstDayWeather(
                            firstday.nowtemp,
                            nowobs,
                            firstday.tidetypeone,
                            firstday.tidetimeone,
                            firstday.tidelevelone,
                            firstday.tidetypetwo,
                            firstday.tidetimetwo,
                            firstday.tideleveltwo,
                            firstday.tidetypethree,
                            firstday.tidetimethree,
                            firstday.tidelevelthree,
                            firstday.tidetypefour,
                            firstday.tidetimefour,
                            firstday.tidelevelfour
                        )

                        val otherday = nowdata.otherDayWeather
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
                        if(i == totaldata.size) {
                            sharedPref.edit().run {
                                putBoolean("getdatabase", true)
                            }.commit()
                        }
                    }
                }

            }
            override fun onFailure(call: Call<List<TotalWeatherDB>>, t: Throwable) {
                Log.d("SQLite Test", "failed")
            }
        })

        Handler().postDelayed(Runnable {
            val intent = Intent(this@IntroActivity, MainActivity::class.java)
            Log.d("application111", "intro start")
            Toast.makeText(this@IntroActivity, "날씨 정보를 받아오는 동안 날씨 기능이 작동하지 않을 수 있습니다.", Toast.LENGTH_LONG).show()
            startActivity(intent)
            finish() }, 4000L)

//        val intent = Intent(this@IntroActivity, MainActivity::class.java)
//        Log.d("application111", "called3")
//        finish()
//        Log.d("application111", "called4")
//        startActivity(intent)
//        Log.d("application111", "called5")
    }

    fun serviceStop()
    {
        val intent = Intent(this, WeatherService::class.java)
        stopService(intent)
    }

}