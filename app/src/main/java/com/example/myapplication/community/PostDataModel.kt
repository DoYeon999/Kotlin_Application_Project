package com.example.myapplication.community

import java.io.Serializable

/**
 * 게시글을 저장할때 필요한 데이터 클래스
 */
data class PostDataModel(
    var id: String = "",
    var nickname : String = "",
    var title: String = "",
    var content: String = "",
    var password: String = "",
    var replies: ArrayList<Replies> = ArrayList(),
    var pictures: ArrayList<String> = ArrayList(),
    var favoriteCount: Int = 0,
    var favorites : MutableMap<String, Boolean> = HashMap()
) : Serializable