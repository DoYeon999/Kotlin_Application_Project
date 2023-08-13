package com.example.myapplication.kdy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityFishpageBinding
import com.example.myapplication.kdy.model.FishInfoModel

class FishpageActivity : AppCompatActivity() {

    lateinit var binding: ActivityFishpageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityFishpageBinding.inflate(layoutInflater)

        val fish = (intent.getSerializableExtra("fish") as FishInfoModel)

        Glide.with(this@FishpageActivity)
            .load(fish.imgurl)
            .into(binding.imgurl)
        binding.fishname.text = fish.fishname
        binding.fishinfo.text = "생김새 : " + fish.fishinfo
        binding.fishsize.text = "금지체장 : " + fish.fishsize
        binding.fishdate.text = "금어기 : " + fish.fishdate
        binding.fishpopular.text = "낚시시즌 : " + fish.fishpopular
        binding.fishplace.text = "서식지 : " + fish.fishplace
        binding.fisheat.text = "미끼추천 : " + fish.fisheat

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}