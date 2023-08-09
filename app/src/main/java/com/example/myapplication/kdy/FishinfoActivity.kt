package com.example.myapplication.kdy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityFishinfoBinding
import com.example.myapplication.kdy.adapter.FishAdapter

class FishinfoActivity : AppCompatActivity() {

    lateinit var binding: ActivityFishinfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityFishinfoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val adapter = FishAdapter(this)
        binding.view.adapter = adapter
    }
}