package com.example.myapplication.sqliteProcess

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityTestBinding
import com.example.myapplication.weather_imgfind.model.TidePreModelDB
import com.example.myapplication.weather_imgfind.model.TotalWeatherDB
import com.example.myapplication.weather_imgfind.net.APIApplication
import com.example.myapplication.weather_imgfind.weather.MapActivity
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestActivity : AppCompatActivity() {
    lateinit var binding : ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val context : Context = this@TestActivity
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
                }
                override fun onFailure(call: Call<TotalWeatherDB>, t: Throwable) {
                    Log.d("SQLite Test", "failed")
                }
            })
        }

    }
}