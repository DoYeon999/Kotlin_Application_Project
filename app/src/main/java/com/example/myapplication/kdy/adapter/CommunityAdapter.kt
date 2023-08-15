package com.example.myapplication.kdy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemCommuityImagesBinding
import com.example.myapplication.databinding.ItemFishinfoBinding
import com.example.myapplication.kdy.FishinfoActivity

class CommunityViewHolder (val binding : ItemCommuityImagesBinding) : RecyclerView.ViewHolder(binding.root)

class CommunityAdapter (val datas : MutableList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int{
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CommunityViewHolder(ItemCommuityImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as CommunityViewHolder).binding
        Glide.with(binding.root).load(datas.get(position)).into(binding.commimg)
    }
}