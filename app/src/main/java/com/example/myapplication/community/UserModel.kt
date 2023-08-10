package com.example.myapplication.community

/**
 * 회원가입시에 유저의 데이터 형식을 firebase에 보내는 데이터 클래스
 */
class UserModel (
    var email: String = "",
    var password: String = "",
    var nickname : String = ""
)