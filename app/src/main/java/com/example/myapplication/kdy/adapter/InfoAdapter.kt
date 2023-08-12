package com.example.myapplication.kdy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemFishinfoBinding
import com.example.myapplication.kdy.FishinfoActivity

class InfoViewHolder (val binding : ItemFishinfoBinding) : RecyclerView.ViewHolder(binding.root)

class InfoAdapter (val datas : MutableList<FishinfoActivity.Info>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int{
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InfoViewHolder(ItemFishinfoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as InfoViewHolder).binding
        Glide.with(binding.root).load(datas!![position].commuimg).into(binding.fishInfo)
    }
}