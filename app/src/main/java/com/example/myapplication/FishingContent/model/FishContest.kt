package com.example.myapplication.FishingContent.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FishContest(
    @SerializedName("fcid")
    var fcid : Long,
    @SerializedName("contestname")
    var contesttitle : String,
    @SerializedName("contestdate")
    var contestdate : String,
    @SerializedName("posterimgurl")
    var contestthumbnail : String
) : Serializable
