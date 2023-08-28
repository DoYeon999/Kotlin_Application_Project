package com.example.myapplication.kdy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.MainActivity
import com.example.myapplication.databinding.ItemMainfishBinding

class MainViewHolder (val binding : ItemMainfishBinding) : RecyclerView.ViewHolder(binding.root)
class MainAdapter ( val datas : MutableList<MainActivity.Main>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int{
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MainViewHolder(ItemMainfishBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as MainViewHolder).binding
        binding.mainfishname.text = datas!![position].fishname
        Glide.with(binding.root).load(datas!![position].fishimg).into(binding.mainfishimg)
    }
}

