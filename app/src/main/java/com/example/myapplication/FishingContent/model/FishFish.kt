package com.example.myapplication.FishingContent.model

import com.google.gson.annotations.SerializedName

class FishFish (
    @SerializedName("ffid")
    var ffid : Long,
    @SerializedName("fishtitle")
    var fishtitle : String,
    @SerializedName("fishvideourl")
    var fishvideourl : String,
    @SerializedName("fishthumbnail")
    var fishthumbnail : String
)
