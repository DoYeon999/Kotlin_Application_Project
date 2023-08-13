package com.example.myapplication.FishingContent.recycler

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.FishingContent.model.FishBait
import com.example.myapplication.FishingContent.model.FishRope
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemNewbieBinding
import com.example.myapplication.FishingContent.model.Guide

class NewbieViewHolder2(val binding: ItemNewbieBinding): RecyclerView.ViewHolder(binding.root)

class NewbieAdpater2(val guides: List<FishBait>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var myview : View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d("tt", "ASdfasdfashafdgasdfiasdofisdaf")
        myview = LayoutInflater.from(parent.context).inflate(R.layout.item_newbie, parent, false)
        return NewbieViewHolder2(ItemNewbieBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("tt22tt", "$position")
        Log.d("tt22tt", "${guides[position]}")
        val nowGuide = guides[position]
        Log.d("tt22tt", "$nowGuide")
        val binding=(holder as NewbieViewHolder2).binding
        Glide.with(myview.context).load(nowGuide.baitthumbnail).into(binding.youtubeThumbnail)
        binding.youtubeThumbnail.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(nowGuide.baitvideourl))
            intent.setPackage("com.google.android.youtube") // 유튜브 앱의 패키지명
            myview.context.startActivity(intent)
        }
        binding.youtubeTitle.text = nowGuide.baittitle
    }

    override fun getItemCount() = guides.size
}
