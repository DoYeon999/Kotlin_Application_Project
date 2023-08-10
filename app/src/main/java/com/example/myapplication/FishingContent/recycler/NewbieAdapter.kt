package com.example.myapplication.FishingContent.recycler

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlin_application_project.R
import com.example.kotlin_application_project.databinding.ItemNewbieBinding
import com.example.myapplication.FishingContent.model.Guide
import com.google.firebase.storage.FirebaseStorage

class NewbieViewHolder(val binding: ItemNewbieBinding): RecyclerView.ViewHolder(binding.root)

class NewbieAdpater(val guides: List<Guide>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var myview : View
    private val storageReference = FirebaseStorage.getInstance().reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d("tt", "ASdfasdfashafdgasdfiasdofisdaf")
        myview = LayoutInflater.from(parent.context).inflate(R.layout.item_newbie, parent, false)
        return NewbieViewHolder(ItemNewbieBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("tt22tt", "$position")
        Log.d("tt22tt", "${guides[position]}")
        val nowGuide = guides[position]
        Log.d("tt22tt", "$nowGuide")
        val imageRef = storageReference.child(nowGuide.thumbnail)
        val binding=(holder as NewbieViewHolder).binding
        imageRef.downloadUrl.addOnSuccessListener { uri
            -> val nowUrl = uri.toString()
            Glide.with(myview.context)
                .load(nowUrl)
                .into(binding.youtubeThumbnail)
        }
        binding.youtubeThumbnail.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(nowGuide.url))
            intent.setPackage("com.google.android.youtube") // 유튜브 앱의 패키지명
            myview.context.startActivity(intent)
        }
        binding.youtubeTitle.text = nowGuide.title
    }

    override fun getItemCount() = guides.size
}
