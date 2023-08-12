package com.example.myapplication.kdy

import android.app.Application
import com.example.myapplication.kdy.model.FishService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIApplication : Application() {

    var fishService : FishService

    val retrofit3 : Retrofit
        get() = Retrofit.Builder()
            .baseUrl("http://10.100.103.74:8088/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    init {
        fishService = retrofit3.create(FishService::class.java)
    }
}