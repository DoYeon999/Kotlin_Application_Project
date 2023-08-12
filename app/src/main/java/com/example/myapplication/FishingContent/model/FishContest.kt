package com.example.myapplication.FishingContent.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FishContest(
    @SerializedName("fcid")
    var fcid : Long,
    @SerializedName("contesttitle")
    var contesttitle : String,
    @SerializedName("contestvideourl")
    var contestvideourl : String,
    @SerializedName("contestthumbnail")
    var contestthumbnail : String
) : Serializable
