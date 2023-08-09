package com.example.myapplication.kdy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityFishpageBinding

class FishpageActivity : AppCompatActivity() {

    lateinit var binding: ActivityFishpageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityFishpageBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}