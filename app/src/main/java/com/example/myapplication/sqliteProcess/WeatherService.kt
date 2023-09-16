package com.example.myapplication.sqliteProcess

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityTestBinding
import com.example.myapplication.weather_imgfind.model.TotalWeatherDB
import com.example.myapplication.weather_imgfind.net.APIApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherService : Service() {
    lateinit var binding : ActivityTestBinding
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //super.onCreate(savedInstanceState)
        //binding = ActivityTestBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        val context : Context = this@WeatherService
        val dbHelper = CustomDatabaseHelper(context)
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
                    runBlocking {
                        launch(Dispatchers.IO) {
                            tide!!.forEach {
                                dbHelper.insertTide(it.obscode, it.tidelevel)
                            }
                        }
                    }

                    val firstday = response.body()!!.firstDayWeather
                    runBlocking {
                        launch (Dispatchers.IO) {
                            dbHelper.insertFirstDayWeather(
                                firstday.nowtemp, nowobs, firstday.tidetypeone, firstday.tidetimeone, firstday.tidelevelone,
                                firstday.tidetypetwo, firstday.tidetimetwo, firstday.tideleveltwo,
                                firstday.tidetypethree, firstday.tidetimethree, firstday.tidelevelthree,
                                firstday.tidetypefour, firstday.tidetimefour, firstday.tidelevelfour
                            )
                        }
                    }

                    val otherday = response.body()!!.otherDayWeather
                    var idx = 1
                    runBlocking {
                        launch (Dispatchers.IO) {
                            otherday.forEach {
                                Log.d("sqlite", "****${it.tidetypefour}****")
                                dbHelper.insertOtherDayWeather(
                                    idx, it.mintemp, it.maxtemp, nowobs,
                                    it.tidetypeone, it.tidetimeone, it.tidelevelone,
                                    it.tidetypetwo, it.tidetimetwo, it.tideleveltwo,
                                    it.tidetypethree, it.tidetimethree, it.tidelevelthree,
                                    it.tidetypefour, it.tidetimethree, it.tidelevelfour
                                )
                                idx++
                            }
                        }
                    }

                    Log.d("SQLite Test", "success $i")
                    i++
                    if(i == obscodelist.size) stopSelf()
                }
                override fun onFailure(call: Call<TotalWeatherDB>, t: Throwable) {
                    Log.d("SQLite Test", "failed")
                }
            })
        }
        Log.d("google1234", "finished!!")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        Log.d("service", "destroyed")
        super.onDestroy()
    }

}