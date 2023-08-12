package com.example.myapplication.kdy.model

import com.example.myapplication.kdy.model.FishInfoModel
import retrofit2.http.GET
import retrofit2.http.Query

interface FishService {

    @GET("fish/fishlist")
    fun getFishList(
    ) : retrofit2.Call<List<FishInfoModel>>

    @GET("fish/fishinfo")
    fun getFishInfo(
        @Query("fid") fid : Long
    ) : retrofit2.Call<FishInfoModel>

}