package com.example.myapplication.kdy

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.MainActivity
import com.example.myapplication.MypageActivity
import com.example.myapplication.R
import com.example.myapplication.community.HomeActivity
import com.example.myapplication.databinding.ActivityFishinfoBinding
import com.example.myapplication.kdy.adapter.FishAdapter
import com.example.myapplication.kdy.adapter.InfoAdapter
import com.example.myapplication.weather_imgfind.weather.MapActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class FishinfoActivity : AppCompatActivity() {

    lateinit var binding: ActivityFishinfoBinding
    data class Info(val commuimg : String)
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityFishinfoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        findViewById<ImageView>(R.id.logomain).setOnClickListener{
            val intent = Intent(this@FishinfoActivity, MainActivity::class.java)
            startActivity(intent)
        }
        findViewById<TextView>(R.id.activitytitle).text = "낚시박사"
        findViewById<ImageView>(R.id.backbtn).setOnClickListener { finish() }
        val sharedPref = getSharedPreferences("logininfo", Context.MODE_PRIVATE)
        val nick = sharedPref.getString("nickname", "")
        val url = sharedPref.getString("profileuri", "")
        val logincheck = sharedPref.getBoolean("signedup", false)
        if(logincheck) {
            findViewById<TextView>(R.id.toolbarnick).text = nick
            findViewById<TextView>(R.id.loginbuttonmain).visibility = View.GONE
            findViewById<TextView>(R.id.toolbarnick).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.toolbarprofile).visibility = View.VISIBLE
            if(url != "" && url != "null") {
                Glide.with(this)
                    .load(url)
                    .into(findViewById(R.id.toolbarprofile))
            }
        }

        // 네비게이션바 페이지 이동
        findViewById<ImageView>(R.id.homepage).setOnClickListener{
            val intent = Intent(this@FishinfoActivity, MainActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.weatherpage).setOnClickListener{
            val intent = Intent(this@FishinfoActivity, MapActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.cumunitypage).setOnClickListener{
            if(logincheck) {
                val intent = Intent(this@FishinfoActivity, HomeActivity::class.java)
                startActivity(intent)
            } else {
                binding.fishinfolayout.alpha = 0.2f
                val dialog = AlertDialog.Builder(this).run {
                    setMessage("로그인한 사용자만 이용할 수 있는 기능입니다.")
                        .setPositiveButton("로그인하기") { it, now ->
                            val intent = Intent(this@FishinfoActivity, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        .setNegativeButton("취소") { it, now ->
                            it.dismiss()
                            //val opacity = ContextCompat.getColor(this@MainActivity, R.color.opacity_100)
                            binding.fishinfolayout.alpha = 1.0f
                        }
                }
                dialog.setCancelable(false)
                dialog.show()
            }
        }

        findViewById<ImageView>(R.id.mypage).setOnClickListener{
            if(logincheck) {
                val intent = Intent(this@FishinfoActivity, MypageActivity::class.java)
                startActivity(intent)
            } else {
                binding.fishinfolayout.alpha = 0.2f
                val dialog = AlertDialog.Builder(this).run {
                    setMessage("로그인한 사용자만 이용할 수 있는 기능입니다.")
                        .setPositiveButton("로그인하기") { it, now ->
                            val intent = Intent(this@FishinfoActivity, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        .setNegativeButton("취소") { it, now ->
                            it.dismiss()
                            //val opacity = ContextCompat.getColor(this@MainActivity, R.color.opacity_100)
                            binding.fishinfolayout.alpha = 1.0f
                        }
                }
                dialog.setCancelable(false)
                dialog.show()
            }
        }

        val adapter = FishAdapter(this)
        binding.view.adapter = adapter

        val database = Firebase.firestore
        val docList = database.collection("fishinfoimg")
        var fishinginfo = mutableListOf<Info>()
        var count = 0
        docList.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val docs = document.documents
                    var checkflag = true
                    docs.forEach {
                        if(!checkflag) return@forEach
                        if(it.exists()) {
                            val storage = FirebaseStorage.getInstance("gs://fishing-4f003.appspot.com")
                            val storageRef = storage.reference.child(it.data?.get("commuimg").toString())
                            storageRef.downloadUrl.addOnSuccessListener { uri -> fishinginfo.add(Info(uri.toString()))
                                    count++
                            if (docs.size == count) {
                                val infoAdapter = InfoAdapter(fishinginfo)
                                val linearLayoutManager = LinearLayoutManager(this)
                                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                                binding.info.layoutManager = linearLayoutManager
                                binding.info.adapter = infoAdapter
                            }
                            }
                                }
                        }
                    }

                }
            }

    }