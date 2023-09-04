package com.example.myapplication.weather_imgfind.model

import com.google.gson.annotations.SerializedName

data class TidePreModelDB(
    @SerializedName("level")
    var tidelevel : String
)