package com.example.myapplication.kdy

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.myapplication.MainActivity
import com.example.myapplication.MypageActivity
import com.example.myapplication.R
import com.example.myapplication.community.HomeActivity
import com.example.myapplication.databinding.ActivityFishpageBinding
import com.example.myapplication.kdy.model.FishInfoModel
import com.example.myapplication.weather_imgfind.weather.MapActivity

class FishpageActivity : AppCompatActivity() {

    lateinit var binding: ActivityFishpageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityFishpageBinding.inflate(layoutInflater)

        val fish = (intent.getSerializableExtra("fish") as FishInfoModel)

        Glide.with(this@FishpageActivity)
            .load(fish.imgurl)
            .into(binding.imgurl)
        //binding.fishname.text = fish.fishname
        binding.fishinfo.text = "생김새 : " + fish.fishinfo
        binding.fishsize.text = "금지체장 : " + fish.fishsize
        binding.fishdate.text = "금어기 : " + fish.fishdate
        binding.fishpopular.text = "낚시시즌 : " + fish.fishpopular
        binding.fishplace.text = "서식지 : " + fish.fishplace
        binding.fisheat.text = "미끼추천 : " + fish.fisheat

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        findViewById<ImageView>(R.id.logomain).setOnClickListener{
            val intent = Intent(this@FishpageActivity, MainActivity::class.java)
            startActivity(intent)
        }
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
        findViewById<TextView>(R.id.activitytitle).text = fish.fishname
        findViewById<ImageView>(R.id.backbtn).setOnClickListener { finish() }

        // 로고 클릭 시
        findViewById<ImageView>(R.id.logomain).setOnClickListener {
            val intent = Intent(this@FishpageActivity, MainActivity::class.java)
            startActivity(intent)
        }

        // 네비게이션바 페이지 이동
        findViewById<ImageView>(R.id.homepage).setOnClickListener{
            val intent = Intent(this@FishpageActivity, MainActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.weatherpage).setOnClickListener{
            val intent = Intent(this@FishpageActivity, MapActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.cumunitypage).setOnClickListener{
            if(logincheck) {
                val intent = Intent(this@FishpageActivity, HomeActivity::class.java)
                startActivity(intent)
            } else {
                binding.fishpagelayout.alpha = 0.2f
                val dialog = AlertDialog.Builder(this).run {
                    setMessage("로그인한 사용자만 이용할 수 있는 기능입니다.")
                        .setPositiveButton("로그인하기") { it, now ->
                            val intent = Intent(this@FishpageActivity, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        .setNegativeButton("취소") { it, now ->
                            it.dismiss()
                            //val opacity = ContextCompat.getColor(this@MainActivity, R.color.opacity_100)
                            binding.fishpagelayout.alpha = 1.0f
                        }
                }
                dialog.setCancelable(false)
                dialog.show()
            }
        }

        findViewById<ImageView>(R.id.mypage).setOnClickListener{
            if(logincheck) {
                val intent = Intent(this@FishpageActivity, MypageActivity::class.java)
                startActivity(intent)
            } else {
                binding.fishpagelayout.alpha = 0.2f
                val dialog = AlertDialog.Builder(this).run {
                    setMessage("로그인한 사용자만 이용할 수 있는 기능입니다.")
                        .setPositiveButton("로그인하기") { it, now ->
                            val intent = Intent(this@FishpageActivity, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        .setNegativeButton("취소") { it, now ->
                            it.dismiss()
                            //val opacity = ContextCompat.getColor(this@MainActivity, R.color.opacity_100)
                            binding.fishpagelayout.alpha = 1.0f
                        }
                }
                dialog.setCancelable(false)
                dialog.show()
            }
        }
    }
}