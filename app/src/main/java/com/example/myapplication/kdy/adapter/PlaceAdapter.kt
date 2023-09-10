package com.example.myapplication.kdy.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemFishplaceBinding
import com.example.myapplication.kdy.FishplaceActivity

class MyViewHolder(val binding: ItemFishplaceBinding): RecyclerView.ViewHolder(binding.root)

class PlaceAdapter (val datas: MutableList<FishplaceActivity.Place>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private lateinit var context : Context

    //리스트 수
    override fun getItemCount(): Int{
        return datas?.size ?: 0
    }

    //뷰 홀더 만들고 뷰 초기화
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = LayoutInflater.from(parent.context).inflate(R.layout.activity_fishplace, parent, false).context
        return MyViewHolder(ItemFishplaceBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    //뷰, 데이터 결합
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as MyViewHolder).binding
        val nowtel = datas!![position].tel.split("-")
        //var dialogtel = "tel:"
        //for (tel in nowtel) dialogtel += tel
        //binding.tel.setOnClickListener {
        //    val intent = Intent(Intent.ACTION_DIAL)
        //    intent.data = Uri.parse(dialogtel)
        //    context.startActivity(intent)
        //
        //}
        //Log.d("place", "$dialogtel")
        binding.name.text= datas!![position].name
        binding.fish.text= datas!![position].fish
        //binding.tel.text= "예약번호\n" + datas!![position].tel
        Glide.with(binding.root).load(datas!![position].fishimgurl).into(binding.fishingImg)
    }
}