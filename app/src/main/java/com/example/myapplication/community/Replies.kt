package com.example.myapplication.community

import java.io.Serializable

/**
 * 게시글의 댓글을 작성할때 서버에 전송하는 데이터 클래스
 */
class Replies(
    var reply_id: String,
    var reply : String
) : Serializable
