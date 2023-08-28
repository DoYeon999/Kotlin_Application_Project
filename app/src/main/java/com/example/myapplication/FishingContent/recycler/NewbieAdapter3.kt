package com.example.myapplication.FishingContent.recycler

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.FishingContent.model.FishFish
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemNewbieBinding

class NewbieViewHolder3(val binding: ItemNewbieBinding): RecyclerView.ViewHolder(binding.root)

class NewbieAdpater3(val guides: List<FishFish>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var myview : View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d("tt", "ASdfasdfashafdgasdfiasdofisdaf")
        myview = LayoutInflater.from(parent.context).inflate(R.layout.item_newbie, parent, false)
        return NewbieViewHolder3(ItemNewbieBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("tt22tt0", "$position")
        Log.d("tt22tt0", "${guides[position]}")
        val nowGuide = guides[position]
        Log.d("tt22tt0", "$nowGuide")
        val binding=(holder as NewbieViewHolder3).binding
        Glide.with(myview.context).load(nowGuide.fishthumbnail).into(binding.youtubeThumbnail)
        binding.youtubeThumbnail.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(nowGuide.fishvideourl))
            intent.setPackage("com.google.android.youtube") // 유튜브 앱의 패키지명
            myview.context.startActivity(intent)
        }
        binding.youtubeTitle.text = nowGuide.fishtitle
    }

    override fun getItemCount() = guides.size
}