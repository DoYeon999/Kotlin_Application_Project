package com.example.myapplication.kdy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityFishplaceBinding
import com.example.myapplication.kdy.adapter.PlaceAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class FishplaceActivity : AppCompatActivity() {

    lateinit var binding : ActivityFishplaceBinding

    data class Place (val fish : String, val name : String, val tel : String, val fishimgurl : String)

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityFishplaceBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        findViewById<ImageView>(R.id.logomain).setOnClickListener{
            val intent = Intent(this@FishplaceActivity, MainActivity::class.java)
            startActivity(intent)
        }
        findViewById<TextView>(R.id.activitytitle).text = "낚시포인트"
        findViewById<ImageView>(R.id.backbtn).setOnClickListener { finish() }

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
        }
        databaseCallfunc("영흥도")
        binding.yeongheundo.setOnClickListener {
            databaseCallfunc("영흥도")
        }
        binding.sachun.setOnClickListener {
            databaseCallfunc("사천")
        }
        binding.geoje.setOnClickListener {
            databaseCallfunc("거제")
        }
        binding.tongyeong.setOnClickListener {
            databaseCallfunc("인천")
        }
        binding.yeosu.setOnClickListener {
            databaseCallfunc("태안")
        }

    }
    fun databaseCallfunc(docName : String) {
        val database = Firebase.firestore
        val docList = database.collection(docName)
        var count = 0
        var fishingplace = mutableListOf<Place>()
        docList.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val docs = document.documents
                    var checkflag = true
                    docs.forEach {
                        if(!checkflag) return@forEach
                        if(it.exists()) {
                            val storage =
                                FirebaseStorage.getInstance("gs://fishing-4f003.appspot.com")
                            val storageRef =
                                storage.reference.child(it.data?.get("fishimgurl").toString())
                            storageRef.downloadUrl
                                .addOnSuccessListener { uri ->
                                    fishingplace.add(
                                        Place(
                                            it.data?.get("fish").toString(),
                                            it.data?.get("name").toString(),
                                            it.data?.get("tel").toString(),
                                            uri.toString()
                                        )
                                    )
                                    count++
                                    Log.d("test1234", "$fishingplace")
                                    Log.d("test1234", "${docs.size}, $count")
                                    if(docs.size == count) {
                                        Log.d("test1234", "asdfsadfsadfsadf")
                                        fishingplacefunc(fishingplace)
                                        checkflag = false
                                    }
                                }
                        }
                    }

                }
            }
    }


    fun fishingplacefunc(places : MutableList<Place>) {
        val placeAdpater = PlaceAdapter(places)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.place.layoutManager = linearLayoutManager
        binding.place.adapter = placeAdpater
    }
}