package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.myapplication.community.HomeActivity
import com.example.myapplication.databinding.ActivityMypageBinding
import com.example.myapplication.kdy.LoginActivity
import com.example.myapplication.kdy.LoginModifyActivity
import com.example.myapplication.weather_imgfind.weather.MapActivity

class MypageActivity : AppCompatActivity() {

    lateinit var binding: ActivityMypageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityMypageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // sharedPreference에서 데이터 받아와서 닉네임/프로필사진 띄움
        val sharedPref = getSharedPreferences("logininfo", Context.MODE_PRIVATE)
        val nick = sharedPref.getString("nickname", "")
        val url = sharedPref.getString("profileuri", "")

        findViewById<TextView>(R.id.mypagename).text = nick
        if(url != "") {
            Glide.with(this)
                .load(url)
                .into(findViewById(R.id.profileImage))
        }

        binding.logout.setOnClickListener {
            val intent = Intent(this@MypageActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.modify.setOnClickListener {
            val intent = Intent(this@MypageActivity, LoginModifyActivity::class.java)
            startActivity(intent)
        }



        // 네비게이션바 페이지 이동
        findViewById<ImageView>(R.id.homepage).setOnClickListener{
            val intent = Intent(this@MypageActivity, MainActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.weatherpage).setOnClickListener{
            val intent = Intent(this@MypageActivity, MapActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.cumunitypage).setOnClickListener{
            val intent = Intent(this@MypageActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.mypage).setOnClickListener{
            val intent = Intent(this@MypageActivity, MypageActivity::class.java)
            startActivity(intent)
        }

    }
}