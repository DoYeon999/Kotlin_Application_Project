package com.example.myapplication.FishingContent.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Poster(
    @SerializedName("title")
    val title: String,
    @SerializedName("date")
    val date : String,
    @SerializedName("ps")
    val ps: String ) : Serializable