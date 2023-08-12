package com.example.myapplication.weather_imgfind.model

import com.google.gson.annotations.SerializedName

data class TideModel (
    var result : Result
)

data class Result (
    var data : List<TideInfo>,
    var meta : Meta
)

data class Meta (
    @SerializedName("obs_post_id")
    var postid : String,
    @SerializedName("obs_post_name")
    var postname : String
)

data class TideInfo(
    @SerializedName("tph_level")
    var tidelevel : String,
    @SerializedName("tph_time")
    val tidetime  : String,
    @SerializedName("hl_code")
    val tidetype  : String
)