package com.example.myapplication.FishingContent.recycler

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.FishingContent.FishingContentPoster
import com.example.myapplication.FishingContent.model.FishContest
import com.example.myapplication.FishingContent.model.Poster
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemPosterBinding
import com.google.firebase.storage.FirebaseStorage

class PosterViewHolder(val binding: ItemPosterBinding): RecyclerView.ViewHolder(binding.root)

class PosterAdapter(val posters:List<FishContest>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private lateinit var fsview : View
    private lateinit var nowview : View
    private val storageReference = FirebaseStorage.getInstance().reference
    //private lateinit var fsview2 : View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        //fsview = LayoutInflater.from(parent.context).inflate(R.layout.item_poster,parent,false)
        //fsview2 = LayoutInflater.from(parent.context).inflate(R.layout.item_poster2,parent,false)
        fsview = LayoutInflater.from(parent.context).inflate(R.layout.activity_fishing_content_poster, parent, false)
        nowview = LayoutInflater.from(parent.context).inflate(R.layout.activity_fishing_content, parent, false)
        return PosterViewHolder(ItemPosterBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("tt22tt", "$position")
        Log.d("tt22tt", "${posters[position]}")
        val poster = posters[position]
        Log.d("tt22tt", "$poster")
//        val imageRef = storageReference.child(nowGuide.ps)
        val binding=(holder as PosterViewHolder).binding
        binding.title.text = poster.contesttitle
        binding.date.text = poster.contestdate
//        imageRef.downloadUrl.addOnSuccessListener { uri
//            -> val nowUrl = uri.toString()
//            Glide.with(fsview.context)
//                .load(nowUrl)
////                .into(binding.ps)
//        }

//        binding.ps.setOnClickListener {
//            val intent = Intent(Intent.ACTION_VIEW,Uri.parse(nowGuide.ps))
//            fsview.context.startActivity(intent)
//        }

//        binding.title.setOnClickListener {
//            val dialog = Dialog(binding.title.context)
//            dialog.setContentView(binding.root)
//            val intent = Intent(Intent.ACTION_VIEW,Uri.parse(nowGuide.ps))
//            fsview2.context.startActivity(intent)
//        }

        binding.contentLayout.setOnClickListener {
            val intent = Intent(fsview.context, FishingContentPoster::class.java)
            intent.putExtra("posterimg", poster)
            nowview.context.startActivity(intent)

            }
        }

//        binding.title.text = nowGuide.title
//    }


    override fun getItemCount() = posters.size

}