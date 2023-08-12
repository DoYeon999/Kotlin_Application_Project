package com.example.myapplication.weather_imgfind.net

import android.app.Application

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class APIApplication : Application(){

    var tideService : NetworkService
    var temperService : TemperService

    val retrofit : Retrofit
        get() = Retrofit.Builder()
            .baseUrl("http://www.khoa.go.kr/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val retrofit2 : Retrofit
        get() = Retrofit.Builder()
            .baseUrl("http://apis.data.go.kr/1360000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    init {
        tideService = retrofit.create(NetworkService::class.java)
        temperService = retrofit2.create(TemperService::class.java)
    }

}