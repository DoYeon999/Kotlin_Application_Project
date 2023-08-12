package com.example.myapplication.FishingContent

import com.example.myapplication.FishingContent.model.FishBait
import com.example.myapplication.FishingContent.model.FishContest
import com.example.myapplication.FishingContent.model.FishRope
import com.example.myapplication.kdy.model.FishInfoModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FishService {

    @GET("fish/fishropelist")
    fun fishRopeList() : Call<List<FishRope>>

    @GET("fish/fishropedetail")
    fun fishRopeDetail(
        @Query("frid") frid : Long) : Call<FishRope>

    @GET("fish/fishbaitlist")
    fun fishBaitList() : Call<List<FishBait>>

    @GET("fish/fishbaitdetail")
    fun fishBaitDetail(
        @Query("fbid") fbid : Long) : Call<FishBait>

    @GET("fish/fishcontestlist")
    fun fishContestList() : Call<List<FishContest>>

    @GET("fish/fishcontestdetail")
    fun fishContestDetail(
        @Query("fcid") fcid : Long) : Call<FishContest>

    @GET("fish/fishlist")
    fun getFishList(
    ) : retrofit2.Call<List<FishInfoModel>>

    @GET("fish/fishinfo")
    fun getFishInfo(
        @Query("fid") fid : Long
    ) : retrofit2.Call<FishInfoModel>

}