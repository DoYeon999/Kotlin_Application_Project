package com.example.myapplication.weather_imgfind.model

import com.google.gson.annotations.SerializedName

data class ForecastModel (
    var response : ForecastResponse
)

data class ForecastResponse (
    var header : ForecastHeader,
    var body : ForecastBody
)

data class ForecastHeader (
    @SerializedName("resultCode")
    var resultCode : String,
    @SerializedName("resultMsg")
    var resultMsg : String
)

data class ForecastBody (
    @SerializedName("dataType")
    var dataType : String,
    @SerializedName("items")
    var items : Forecast
)

data class Forecast (
    @SerializedName("item")
    var tempFore : List<ForecastTemp>
)

data class ForecastTemp(
    @SerializedName("taMin3")
    var taMin3 : String,
    @SerializedName("taMax3")
    var taMax3 : String,
    @SerializedName("taMin4")
    var taMin4 : String,
    @SerializedName("taMax4")
    var taMax4 : String,
    @SerializedName("taMin5")
    var taMin5 : String,
    @SerializedName("taMax5")
    var taMax5 : String,
    @SerializedName("taMin6")
    var taMin6 : String,
    @SerializedName("taMax6")
    var taMax6 : String
)