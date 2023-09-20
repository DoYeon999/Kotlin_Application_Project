package com.example.myapplication.FishingContent

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.myapplication.FishingContent.model.FishContest
import com.example.myapplication.FishingContent.model.Poster
import com.example.myapplication.MainActivity
import com.example.myapplication.MypageActivity
import com.example.myapplication.R
import com.example.myapplication.community.HomeActivity
import com.example.myapplication.databinding.ActivityFishingContentPosterBinding
import com.example.myapplication.kdy.LoginActivity
import com.example.myapplication.weather_imgfind.weather.MapActivity

class FishingContentPoster : AppCompatActivity() {
    private lateinit var binding : ActivityFishingContentPosterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFishingContentPosterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getSerializableExtra("posterimg") as FishContest
        Glide.with(this@FishingContentPoster)
            .load(data.contestthumbnail)
            .into(binding.ps)
        findViewById<ImageView>(R.id.logomain).setOnClickListener {
            val intent = Intent(this@FishingContentPoster, MainActivity::class.java)
            startActivity(intent)
        }
        findViewById<TextView>(R.id.loginbuttonmain).setOnClickListener {
            val intent = Intent(this@FishingContentPoster, LoginActivity::class.java)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.backbtn).setOnClickListener { finish() }
        findViewById<TextView>(R.id.activitytitle).text = data.contesttitle
        val sharedPref = getSharedPreferences("logininfo", Context.MODE_PRIVATE)
        val nick = sharedPref.getString("nickname", "")
        val url = sharedPref.getString("profileuri", "")
        val logincheck = sharedPref.getBoolean("signedup", false)
        if(logincheck) {
            findViewById<TextView>(R.id.toolbarnick).text = nick
            if(url != "" && url != "null") {
                Glide.with(this)
                    .load(url)
                    .into(findViewById(R.id.toolbarprofile))
            }
            findViewById<TextView>(R.id.loginbuttonmain).visibility = View.GONE
            findViewById<TextView>(R.id.toolbarnick).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.toolbarprofile).visibility = View.VISIBLE
        }

        // 네비게이션바 페이지 이동
        findViewById<ImageView>(R.id.homepage).setOnClickListener{
            val intent = Intent(this@FishingContentPoster, MainActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.weatherpage).setOnClickListener{
            val intent = Intent(this@FishingContentPoster, MapActivity::class.java)
            val checkdataloading = sharedPref.getBoolean("getdatabase", false)
            if(checkdataloading) startActivity(intent)
            else Toast.makeText(this@FishingContentPoster, "데이터를 받아오는 중입니다!", Toast.LENGTH_LONG).show()
        }

        findViewById<ImageView>(R.id.cumunitypage).setOnClickListener{
            if(logincheck) {
                val intent = Intent(this@FishingContentPoster, HomeActivity::class.java)
                startActivity(intent)
            } else {
                binding.posterImgLayout.alpha = 0.2f
                val dialog = AlertDialog.Builder(this).run {
                    setMessage("로그인한 사용자만 이용할 수 있는 기능입니다.")
                        .setPositiveButton("로그인하기") { it, now ->
                            val intent = Intent(this@FishingContentPoster, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        .setNegativeButton("취소") { it, now ->
                            it.dismiss()
                            //val opacity = ContextCompat.getColor(this@MainActivity, R.color.opacity_100)
                            binding.posterImgLayout.alpha = 1.0f
                        }
                }
                dialog.setCancelable(false)
                dialog.show()
            }
        }

        findViewById<ImageView>(R.id.mypage).setOnClickListener{
            if(logincheck) {
                val intent = Intent(this@FishingContentPoster, MypageActivity::class.java)
                startActivity(intent)
            } else {
                binding.posterImgLayout.alpha = 0.2f
                val dialog = AlertDialog.Builder(this).run {
                    setMessage("로그인한 사용자만 이용할 수 있는 기능입니다.")
                        .setPositiveButton("로그인하기") { it, now ->
                            val intent = Intent(this@FishingContentPoster, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        .setNegativeButton("취소") { it, now ->
                            it.dismiss()
                            //val opacity = ContextCompat.getColor(this@MainActivity, R.color.opacity_100)
                            binding.posterImgLayout.alpha = 1.0f
                        }
                }
                dialog.setCancelable(false)
                dialog.show()
            }
        }
    }
}