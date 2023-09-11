package com.example.myapplication.FishingContent

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.FishingContent.model.FishContest
import com.example.myapplication.FishingContent.model.Poster
import com.example.myapplication.FishingContent.recycler.PhDividerItemDecoration
import com.example.myapplication.FishingContent.recycler.PosterAdapter
import com.example.myapplication.MainActivity
import com.example.myapplication.MypageActivity
import com.example.myapplication.R
import com.example.myapplication.community.HomeActivity
import com.example.myapplication.databinding.ActivityFishingContentBinding
import com.example.myapplication.kdy.LoginActivity
import com.example.myapplication.weather_imgfind.net.APIApplication
import com.example.myapplication.weather_imgfind.weather.MapActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class FishingContent : AppCompatActivity() {


    private lateinit var binding: ActivityFishingContentBinding
    lateinit var psAdapter : PosterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFishingContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadFirestoreData()
        findViewById<ImageView>(R.id.logomain).setOnClickListener{
            val intent = Intent(this@FishingContent, MainActivity::class.java)
            startActivity(intent)
        }
        findViewById<TextView>(R.id.activitytitle).text = "낚시 대회"
        findViewById<ImageView>(R.id.backbtn).setOnClickListener {
            finish()
        }
        val sharedPref = getSharedPreferences("logininfo", Context.MODE_PRIVATE)
        val nick = sharedPref.getString("nickname", "")
        val url = sharedPref.getString("profileuri", "")
        val logincheck = sharedPref.getBoolean("signedup", false)
        if(logincheck) {
            findViewById<TextView>(R.id.toolbarnick).text = nick
            if(url != "") {
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
            val intent = Intent(this@FishingContent, MainActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.weatherpage).setOnClickListener{
            val intent = Intent(this@FishingContent, MapActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.cumunitypage).setOnClickListener{
            if(logincheck) {
                val intent = Intent(this@FishingContent, HomeActivity::class.java)
                startActivity(intent)
            } else {
                binding.fishingcontentlayout.alpha = 0.2f
                val dialog = AlertDialog.Builder(this).run {
                    setMessage("로그인한 사용자만 이용할 수 있는 기능입니다.")
                        .setPositiveButton("로그인하기") { it, now ->
                            val intent = Intent(this@FishingContent, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        .setNegativeButton("취소") { it, now ->
                            it.dismiss()
                            //val opacity = ContextCompat.getColor(this@MainActivity, R.color.opacity_100)
                            binding.fishingcontentlayout.alpha = 1.0f
                        }
                }
                dialog.setCancelable(false)
                dialog.show()
            }
        }

        findViewById<ImageView>(R.id.mypage).setOnClickListener{
            if(logincheck) {
                val intent = Intent(this@FishingContent, MypageActivity::class.java)
                startActivity(intent)
            } else {
                binding.fishingcontentlayout.alpha = 0.2f
                val dialog = AlertDialog.Builder(this).run {
                    setMessage("로그인한 사용자만 이용할 수 있는 기능입니다.")
                        .setPositiveButton("로그인하기") { it, now ->
                            val intent = Intent(this@FishingContent, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        .setNegativeButton("취소") { it, now ->
                            it.dismiss()
                            //val opacity = ContextCompat.getColor(this@MainActivity, R.color.opacity_100)
                            binding.fishingcontentlayout.alpha = 1.0f
                        }
                }
                dialog.setCancelable(false)
                dialog.show()
            }
        }
    }



    private fun loadFirestoreData() {
        val fishService = (applicationContext as APIApplication).fishService
        val contest = fishService.fishContestList()
        contest.enqueue(object : Callback<List<FishContest>> {
            override fun onResponse(call: Call<List<FishContest>>, response: Response<List<FishContest>>) {
                val contestdata = response.body()
                viewBindingFunc(contestdata!!)
            }
            override fun onFailure(call: Call<List<FishContest>>, t: Throwable) {
            }
        })

    }
    fun viewBindingFunc(fishes : List<FishContest>) {
        psAdapter = PosterAdapter(fishes)
        binding.recyclerView.adapter = psAdapter
        val linearLayoutManager = PhDividerItemDecoration(5F, Color.LTGRAY)
        binding.recyclerView.addItemDecoration(linearLayoutManager)
    }

}