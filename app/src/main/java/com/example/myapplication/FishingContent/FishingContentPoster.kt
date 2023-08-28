package com.example.myapplication.FishingContent

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.myapplication.FishingContent.model.FishContest
import com.example.myapplication.FishingContent.model.Poster
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityFishingContentPosterBinding

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
        findViewById<ImageView>(R.id.backbtn).setOnClickListener { finish() }
        findViewById<TextView>(R.id.activitytitle).text = data.contesttitle
        val sharedPref = getSharedPreferences("logininfo", Context.MODE_PRIVATE)
        val nick = sharedPref.getString("nickname", "")
        val url = sharedPref.getString("profileuri", "")
        findViewById<TextView>(R.id.toolbarnick).text = "\n" + nick
        if(url != "") {
            Glide.with(this)
                .load(url)
                .into(findViewById(R.id.toolbarprofile))
        }
    }
}