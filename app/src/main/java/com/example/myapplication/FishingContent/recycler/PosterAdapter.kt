package com.example.myapplication.FishingContent.recycler

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.FishingContent.FishingContentPoster
import com.example.myapplication.FishingContent.model.FishContest
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemPosterBinding
import com.example.myapplication.FishingContent.model.Poster
import com.google.firebase.storage.FirebaseStorage

class PosterViewHolder(val binding: ItemPosterBinding): RecyclerView.ViewHolder(binding.root)

class PosterAdapter(val posters:List<FishContest>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private lateinit var fsview : View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        fsview = LayoutInflater.from(parent.context).inflate(R.layout.item_poster,parent,false)
        return PosterViewHolder(ItemPosterBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("tt22tt", "$position")
        Log.d("tt22tt", "${posters[position]}")
        val poster = posters[position]
        Log.d("tt22tt", "$poster")
        //val imageRef = storageReference.child(nowGuide.ps)
        val binding=(holder as PosterViewHolder).binding
        //imageRef.downloadUrl.addOnSuccessListener { uri
        //    -> val nowUrl = uri.toString()
            Glide.with(fsview.context)
                .load(poster.contestthumbnail)
                .into(binding.ps)
        //}

        binding.ps.setOnClickListener {
            val intent = Intent(fsview.context, FishingContentPoster::class.java)
            intent.putExtra("posterimg", poster)
            fsview.context.startActivity(intent)
        }

        binding.title.text = poster.contesttitle
    }

    override fun getItemCount() = posters.size

}