package com.example.myapplication.weather_imgfind.model

import com.google.gson.annotations.SerializedName

data class WeatherModel (
    var response : Response
)

data class Response (
    var header : Header,
    var body : Body
)

data class Header (
    @SerializedName("resultCode")
    var resultCode : String,
    @SerializedName("resultMsg")
    var resultMsg : String
)

data class Body (
    @SerializedName("dataType")
    var dataType : String,
    @SerializedName("items")
    var items : Item
)

data class Item (
    @SerializedName("item")
    var tempPre : List<Temp>
)

data class Temp(
    @SerializedName("baseDate")
    var baseDate : String,
    @SerializedName("baseTime")
    val baseTime  : String,
    @SerializedName("category")
    val category  : String,
    @SerializedName("fcstDate")
    val fcstDate  : String,
    @SerializedName("fcstTime")
    val fcstTime : String,
    @SerializedName("fcstValue")
    val fcstValue : String
)