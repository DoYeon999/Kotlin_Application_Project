package com.example.myapplication.FishingContent.model

import com.google.gson.annotations.SerializedName

data class FishRope(
    @SerializedName("frid")
    var frid : Long,
    @SerializedName("ropetitle")
    var ropetitle : String,
    @SerializedName("ropevideourl")
    var ropevideourl : String,
    @SerializedName("ropethumbnail")
    var ropethumbnail : String
)
