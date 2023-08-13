package com.example.myapplication.kdy.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FishInfoModel(
    @SerializedName("fid")
    var fid : Long,
    @SerializedName("fishname")
    var fishname : String,
    @SerializedName("fishinfo")
    var fishinfo : String,
    @SerializedName("fishdate")
    var fishdate : String,
    @SerializedName("fishsize")
    var fishsize : String,
    @SerializedName("fishpopular")
    var fishpopular : String,
    @SerializedName("fishplace")
    var fishplace : String,
    @SerializedName("fisheat")
    var fisheat : String,
    @SerializedName("imgurl")
    var imgurl : String
) : Serializable
