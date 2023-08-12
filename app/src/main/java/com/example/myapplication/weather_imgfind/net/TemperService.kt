package com.example.myapplication.weather_imgfind.net

import com.example.myapplication.weather_imgfind.model.ForecastModel
import com.example.myapplication.weather_imgfind.model.WeatherModel
import retrofit2.http.GET
import retrofit2.http.Query

interface TemperService {

    @GET("VilageFcstInfoService_2.0/getVilageFcst")
    fun getWeather(
        @Query("ServiceKey") serviceKey : String,
        @Query("pageNo") pageNo : Int,
        @Query("numOfRows") numOfRows : Int,
        @Query("dataType") dataType : String,
        @Query("base_date") baseDate : String,
        @Query("base_time") baseTime : String,
        @Query("nx") nx : Int,
        @Query("ny") ny : Int
    ) : retrofit2.Call<WeatherModel>

    @GET("MidFcstInfoService/getMidTa")
    fun getForecast(
        @Query("ServiceKey") serviceKey : String,
        @Query("pageNo") pageNo : Int,
        @Query("numOfRows") numOfRows: Int,
        @Query("dataType") dataType: String,
        @Query("regId") regId : String,
        @Query("tmFc") tmFc : String
    ) : retrofit2.Call<ForecastModel>

}