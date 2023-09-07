package com.example.myapplication.kdy

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityLoginModifyBinding

class LoginModifyActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginModifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityLoginModifyBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        findViewById<TextView>(R.id.activitytitle).text = "내 정보 수정"

        // sharedPref에서 데이터 받아와서 닉네임, 프로필사진, id, pw 띄움
        val sharedPref = getSharedPreferences("logininfo", Context.MODE_PRIVATE)
        val url = sharedPref.getString("profileuri", "")
        val nick = sharedPref.getString("nickname", "")
        val id = sharedPref.getString("id", "")
        val pw = sharedPref.getString("pw", "")

        if(url != "") {
            Glide.with(this)
                .load(url)
                .into(findViewById(R.id.profileImage))
        }

        //binding.idInput.text

        findViewById<ImageView>(R.id.backbtn).setOnClickListener{
            finish()
        }
    }
}