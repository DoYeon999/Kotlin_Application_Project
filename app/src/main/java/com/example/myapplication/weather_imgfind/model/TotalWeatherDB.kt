package com.example.myapplication.weather_imgfind.model

import com.google.gson.annotations.SerializedName

data class TotalWeatherDB(
    @SerializedName("todayTideDTOList")
    var todaytidelist : List<TidePreModelDB>,
    @SerializedName("firstDayWeatherDTO")
    var firstDayWeather : FirstDayWeatherDB,
    @SerializedName("otherWeatherDTOList")
    var otherDayWeather : OtherDayWeatherDB
)
