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
import com.example.myapplication.databinding.ActivityFishinfoBinding
import com.example.myapplication.kdy.adapter.FishAdapter
import com.example.myapplication.kdy.adapter.InfoAdapter
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
            if(url != "") {
                Glide.with(this)
                    .load(url)
                    .into(findViewById(R.id.toolbarprofile))
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