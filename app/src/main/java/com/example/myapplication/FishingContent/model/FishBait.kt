package com.example.myapplication.FishingContent.model

import com.google.gson.annotations.SerializedName

data class FishBait(
    @SerializedName("fbid")
    var fbid : Long,
    @SerializedName("baittitle")
    var baittitle : String,
    @SerializedName("baitvideourl")
    var baitvideourl : String,
    @SerializedName("baitthumbnail")
    var baitthumbnail : String
)
