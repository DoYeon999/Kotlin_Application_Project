package com.example.myapplication.weather_imgfind.model

import com.google.gson.annotations.SerializedName

data class FirstDayWeatherDB(
    @SerializedName("nowtemp")
    var nowtemp : String,
    @SerializedName("tidelevelOne")
    var tidelevelone : String,
    @SerializedName("tidetimeOne")
    var tidetimeone : String,
    @SerializedName("tidetypeOne")
    var tidetypeone : String,
    @SerializedName("tidelevelTwo")
    var tideleveltwo : String,
    @SerializedName("tidetimeTwo")
    var tidetimetwo : String,
    @SerializedName("tidetypeTwo")
    var tidetypetwo : String,
    @SerializedName("tidelevelThree")
    var tidelevelthree : String,
    @SerializedName("tidetimeThree")
    var tidetimethree : String,
    @SerializedName("tidetypeThree")
    var tidetypethree : String,
    @SerializedName("tidelevelFour")
    var tidelevelfour : String,
    @SerializedName("tidetimeFour")
    var tidetimefour : String,
    @SerializedName("tidetypeFour")
    var tidetypefour : String
)
