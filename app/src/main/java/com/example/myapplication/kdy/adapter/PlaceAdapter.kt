package com.example.myapplication.kdy.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemFishplaceBinding
import com.example.myapplication.kdy.FishplaceActivity
import com.google.firebase.firestore.FirebaseFirestore

class MyViewHolder(val binding: ItemFishplaceBinding): RecyclerView.ViewHolder(binding.root)

class PlaceAdapter (val datas: MutableList<FishplaceActivity.Place>?, val docName : String): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

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

        val sharedPref = context.getSharedPreferences("logininfo", Context.MODE_PRIVATE)
        val id = sharedPref.getString("id", "")
        val signedup = sharedPref.getBoolean("signedup", false)

        if(signedup) {
            val db = FirebaseFirestore.getInstance()
            db.collection(docName).document(datas[position].docId).get().addOnSuccessListener { doc ->
                val nowDocData = doc.data as HashMap<String, Any>
                val nowId = datas[position].docId
                var updateStar = nowDocData.get("star") as HashMap<String, Boolean>
                Log.d("place test", "$nowDocData")
                if(id in (nowDocData.get("star") as HashMap<String, Boolean>).keys) {
                    binding.tostar.visibility = View.GONE
                    binding.unstar.visibility = View.VISIBLE
                    binding.unstar.setOnClickListener {
                        updateStar.remove(id)
                        binding.unstar.visibility = View.GONE
                        binding.tostar.visibility = View.VISIBLE
                        val updatePlace = hashMapOf<String, Any>(
                            "fish" to java.lang.String.valueOf(nowDocData.get("fish")),
                            "fishimgurl" to java.lang.String.valueOf(nowDocData.get("fishimgurl")),
                            "name" to java.lang.String.valueOf(nowDocData.get("name")),
                            "tel" to java.lang.String.valueOf(nowDocData.get("tel")),
                            "star" to updateStar
                        )
                        db.collection(docName).document(nowId).update(updatePlace)
                            .addOnSuccessListener { Toast.makeText(context, "즐겨찾기에서 해제합니다.", Toast.LENGTH_SHORT).show() }
                    }
                    binding.tostar.setOnClickListener {
                        updateStar.put(id!!, true)
                        binding.unstar.visibility = View.VISIBLE
                        binding.tostar.visibility = View.GONE
                        val updatePlace = hashMapOf<String, Any>(
                            "fish" to java.lang.String.valueOf(nowDocData.get("fish")),
                            "fishimgurl" to java.lang.String.valueOf(nowDocData.get("fishimgurl")),
                            "name" to java.lang.String.valueOf(nowDocData.get("name")),
                            "tel" to java.lang.String.valueOf(nowDocData.get("tel")),
                            "star" to updateStar
                        )
                        db.collection(docName).document(nowId).update(updatePlace)
                            .addOnSuccessListener { Toast.makeText(context, "즐겨찾기에 등록합니다.", Toast.LENGTH_SHORT).show() }
                    }
                } else {
                    binding.tostar.visibility = View.VISIBLE
                    binding.unstar.visibility = View.GONE
                    binding.tostar.setOnClickListener {
                        updateStar.put(id!!, true)
                        binding.unstar.visibility = View.VISIBLE
                        binding.tostar.visibility = View.GONE
                        val updatePlace = hashMapOf<String, Any>(
                            "fish" to java.lang.String.valueOf(nowDocData.get("fish")),
                            "fishimgurl" to java.lang.String.valueOf(nowDocData.get("fishimgurl")),
                            "name" to java.lang.String.valueOf(nowDocData.get("name")),
                            "tel" to java.lang.String.valueOf(nowDocData.get("tel")),
                            "star" to updateStar
                        )
                        db.collection(docName).document(nowId).update(updatePlace)
                            .addOnSuccessListener { Toast.makeText(context, "즐겨찾기에 등록합니다.", Toast.LENGTH_SHORT).show() }
                    }
                    binding.unstar.setOnClickListener {
                        updateStar.remove(id)
                        binding.unstar.visibility = View.GONE
                        binding.tostar.visibility = View.VISIBLE
                        val updatePlace = hashMapOf<String, Any>(
                            "fish" to java.lang.String.valueOf(nowDocData.get("fish")),
                            "fishimgurl" to java.lang.String.valueOf(nowDocData.get("fishimgurl")),
                            "name" to java.lang.String.valueOf(nowDocData.get("name")),
                            "tel" to java.lang.String.valueOf(nowDocData.get("tel")),
                            "star" to updateStar
                        )
                        db.collection(docName).document(nowId).update(updatePlace)
                            .addOnSuccessListener { Toast.makeText(context, "즐겨찾기에서 해제합니다.", Toast.LENGTH_SHORT).show() }
                    }
                }
            }
        } else {
            binding.tostar.visibility = View.GONE
        }


        Glide.with(binding.root).load(datas!![position].fishimgurl).into(binding.fishingImg)
    }
}