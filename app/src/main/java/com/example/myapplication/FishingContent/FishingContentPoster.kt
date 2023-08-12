package com.example.myapplication.FishingContent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.kotlin_application_project.R
import com.example.kotlin_application_project.databinding.ActivityFishingContentPosterBinding
import com.example.myapplication.FishingContent.model.Poster

class FishingContentPoster : AppCompatActivity() {
    private lateinit var binding : ActivityFishingContentPosterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFishingContentPosterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getSerializableExtra("posterimg") as Poster
        Glide.with(this@FishingContentPoster)
            .load(data.ps)
            .into(binding.ps)
    }
}