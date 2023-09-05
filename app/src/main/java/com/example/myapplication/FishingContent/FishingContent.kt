package com.example.myapplication.FishingContent

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityFishingContentBinding
import com.example.myapplication.weather_imgfind.net.APIApplication
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