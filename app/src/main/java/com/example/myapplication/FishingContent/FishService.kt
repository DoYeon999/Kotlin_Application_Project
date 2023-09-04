package com.example.myapplication.FishingContent

import com.example.myapplication.FishingContent.model.FishBait
import com.example.myapplication.FishingContent.model.FishContest
import com.example.myapplication.FishingContent.model.FishFish
import com.example.myapplication.FishingContent.model.FishRope
import com.example.myapplication.kdy.model.FishInfoModel
import com.example.myapplication.weather_imgfind.model.TidePreModelDB
import com.example.myapplication.weather_imgfind.model.FirstDayWeatherDB
import com.example.myapplication.weather_imgfind.model.OtherDayWeatherDB
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FishService {

    @GET("fish/fishropelist")
    fun fishRopeList() : Call<List<FishRope>>

    @GET("fish/fishropedetail")
    fun fishRopeDetail(
        @Query("frid") frid : Long) : Call<FishRope>

    @GET("fish/fishbaitlist")
    fun fishBaitList() : Call<List<FishBait>>

    @GET("fish/fishbaitdetail")
    fun fishBaitDetail(
        @Query("fbid") fbid : Long) : Call<FishBait>

    @GET("fish/fishcontestlist")
    fun fishContestList() : Call<List<FishContest>>

    @GET("fish/fishcontestdetail")
    fun fishContestDetail(
        @Query("fcid") fcid : Long) : Call<FishContest>

    @GET("fish/fishfishlist")
    fun fishFishList(): Call<List<FishFish>>

    @GET("fish/fishfishdetail")
    fun fishFishDetail(
        @Query("ffid") ffid:Long) : Call<FishFish>

    @GET("fish/fishlist")
    fun getFishList(
    ) : retrofit2.Call<List<FishInfoModel>>

    @GET("fish/fishinfo")
    fun getFishInfo(
        @Query("fid") fid : Long
    ) : retrofit2.Call<FishInfoModel>

    @GET("weather/getTodayTide")
    fun getTodayTide(
        @Query("obscode") obscode : String
    ) : retrofit2.Call<List<TidePreModelDB>>

    @GET("weather/getPreWeatherFirstDay")
    fun getFirstDayForecast(
        @Query("obscode") obscode : String
    ) : retrofit2.Call<FirstDayWeatherDB>

    @GET("weather/getPreWeatherOtherDay")
    fun getOtherDayForecast(
        @Query("obscode") obscode : String
    ) : retrofit2.Call<List<OtherDayWeatherDB>>
}