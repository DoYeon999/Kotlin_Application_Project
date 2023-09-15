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
import com.example.myapplication.databinding.ActivityStarredPlaceBinding
import com.example.myapplication.kdy.adapter.PlaceAdapter
import com.example.myapplication.kdy.adapter.PlaceAdapter2
import com.example.myapplication.weather_imgfind.weather.MapActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class StarredPlaceActivity : AppCompatActivity() {

    lateinit var binding : ActivityStarredPlaceBinding
    var fishingplace = mutableListOf<Place>()
    val placelist = listOf("거제", "사천", "영흥도", "인천", "태안")
    var nowid : String? = ""

    data class Place (val docId : String, val fish : String,
                      val name : String, val tel : String,
                      val fishimgurl : String, val stars : HashMap<String, Boolean>,
                      val city : String)

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityStarredPlaceBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        findViewById<ImageView>(R.id.logomain).setOnClickListener{
            val intent = Intent(this@StarredPlaceActivity, MainActivity::class.java)
            startActivity(intent)
        }
        findViewById<TextView>(R.id.activitytitle).text = "낚시포인트"
        findViewById<ImageView>(R.id.backbtn).setOnClickListener { finish() }

        val sharedPref = getSharedPreferences("logininfo", Context.MODE_PRIVATE)
        val nick = sharedPref.getString("nickname", "")
        val url = sharedPref.getString("profileuri", "")
        nowid = sharedPref.getString("id", "")
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

        // 로고 클릭 시
        findViewById<ImageView>(R.id.logomain).setOnClickListener {
            val intent = Intent(this@StarredPlaceActivity, MainActivity::class.java)
            startActivity(intent)
        }

        // 네비게이션바 페이지 이동
        findViewById<ImageView>(R.id.homepage).setOnClickListener{
            val intent = Intent(this@StarredPlaceActivity, MainActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.weatherpage).setOnClickListener{
            val intent = Intent(this@StarredPlaceActivity, MapActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.cumunitypage).setOnClickListener{
            if(logincheck) {
                val intent = Intent(this@StarredPlaceActivity, HomeActivity::class.java)
                startActivity(intent)
            } else {
                binding.fishplacelayout.alpha = 0.2f
                val dialog = AlertDialog.Builder(this).run {
                    setMessage("로그인한 사용자만 이용할 수 있는 기능입니다.")
                        .setPositiveButton("로그인하기") { it, now ->
                            val intent = Intent(this@StarredPlaceActivity, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        .setNegativeButton("취소") { it, now ->
                            it.dismiss()
                            //val opacity = ContextCompat.getColor(this@MainActivity, R.color.opacity_100)
                            binding.fishplacelayout.alpha = 1.0f
                        }
                }
                dialog.setCancelable(false)
                dialog.show()
            }
        }

        findViewById<ImageView>(R.id.mypage).setOnClickListener{
            if(logincheck) {
                val intent = Intent(this@StarredPlaceActivity, MypageActivity::class.java)
                startActivity(intent)
            } else {
                binding.fishplacelayout.alpha = 0.2f
                val dialog = AlertDialog.Builder(this).run {
                    setMessage("로그인한 사용자만 이용할 수 있는 기능입니다.")
                        .setPositiveButton("로그인하기") { it, now ->
                            val intent = Intent(this@StarredPlaceActivity, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        .setNegativeButton("취소") { it, now ->
                            it.dismiss()
                            //val opacity = ContextCompat.getColor(this@MainActivity, R.color.opacity_100)
                            binding.fishplacelayout.alpha = 1.0f
                        }
                }
                dialog.setCancelable(false)
                dialog.show()
            }
        }



        for(i in 0 until placelist.size) {
            databaseCallfunc(placelist[i], i)
        }
    }


    fun databaseCallfunc(docName : String, idx : Int) {
        val database = Firebase.firestore
        val docList = database.collection(docName)
        docList.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val docs = document.documents
                    var checkflag = true
                    var count = 0
                    docs.forEach {nowdoc->
                        if(!checkflag) return@forEach
                        if(nowdoc.exists()) {
                            val storage =
                                FirebaseStorage.getInstance("gs://fishing-4f003.appspot.com")
                            val storageRef =
                                storage.reference.child(nowdoc.data?.get("fishimgurl").toString())
                            storageRef.downloadUrl
                                .addOnSuccessListener { uri ->
                                    val stars = nowdoc.data?.get("star") as HashMap<String, Boolean>
                                    if(nowid in stars.keys) {
                                        Log.d("starred", "asdfasdfasdfasdfsad")
                                        fishingplace.add(
                                            Place(
                                                nowdoc.id,
                                                nowdoc.data?.get("fish").toString(),
                                                nowdoc.data?.get("name").toString(),
                                                nowdoc.data?.get("tel").toString(),
                                                uri.toString(),
                                                nowdoc.data?.get("star") as HashMap<String, Boolean>,
                                                docName
                                            )
                                        )
                                    }
                                    Log.d("starred", "$fishingplace")
                                    count++
                                    Log.d("starred", "$count,,,,$idx")
                                    if(count == docs.size && idx == (placelist.size - 1)) {
                                        Log.d("starred", "adapter call")
                                        fishingplacefunc(fishingplace)
                                    }
                                }
                        }
                    }
                }
            }
    }

    fun fishingplacefunc(places : MutableList<Place>) {
        val placeAdpater = PlaceAdapter2(places)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.place.layoutManager = linearLayoutManager
        binding.place.adapter = placeAdpater
    }
}