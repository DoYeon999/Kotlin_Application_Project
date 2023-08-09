package com.example.myapplication.kdy.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.Fragment3Binding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class Fragment3 : Fragment() {

    lateinit var binding: Fragment3Binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Fragment3Binding.inflate(inflater, container, false)
        return binding.root

    }

    data class Fish(val fishName: String, val fishimgurl: String)

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = Fragment3Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val database = Firebase.firestore
        val docList = database.collection("fishing3").orderBy("fishName")


        var fishes = mutableListOf<Fish>()
        docList.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val docs = document.documents
                    var i = 0
                    docs.forEach {
                        Log.d("testtest", "${it.data?.get("fishName")}")
                        Log.d("testtest", "${it.data?.get("fishimgurl")}")
                        val storage =
                            FirebaseStorage.getInstance("gs://fishing-4f003.appspot.com")
                        val storageRef =
                            storage.reference.child(it.data?.get("fishimgurl").toString())
                        storageRef.downloadUrl
                            .addOnSuccessListener { uri ->
                                fishes.add(
                                    Fish(
                                        it.data?.get("fishName").toString(),
                                        uri.toString()
                                    )
                                )

                                if (i == 0) {
                                    Glide.with(this@Fragment3).load(fishes[i].fishimgurl)
                                        .into(binding.img0)
                                    binding.text0.text = fishes[i].fishName
                                } else if (i == 1) {
                                    Glide.with(this@Fragment3).load(fishes[i].fishimgurl)
                                        .into(binding.img1)
                                    binding.text1.text = fishes[i].fishName
                                } else if (i == 2) {
                                    Glide.with(this@Fragment3).load(fishes[i].fishimgurl)
                                        .into(binding.img2)
                                    binding.text2.text = fishes[i].fishName
                                } else if (i == 3) {
                                    Glide.with(this@Fragment3).load(fishes[i].fishimgurl)
                                        .into(binding.img3)
                                    binding.text3.text = fishes[i].fishName
                                } else if (i == 4) {
                                    Glide.with(this@Fragment3).load(fishes[i].fishimgurl)
                                        .into(binding.img4)
                                    binding.text4.text = fishes[i].fishName
                                } else if (i == 5) {
                                    Glide.with(this@Fragment3).load(fishes[i].fishimgurl)
                                        .into(binding.img5)
                                    binding.text5.text = fishes[i].fishName
                                } else if (i == 6) {
                                    Glide.with(this@Fragment3).load(fishes[i].fishimgurl)
                                        .into(binding.img6)
                                    binding.text6.text = fishes[i].fishName
                                } else if (i == 7) {
                                    Glide.with(this@Fragment3).load(fishes[i].fishimgurl)
                                        .into(binding.img7)
                                    binding.text7.text = fishes[i].fishName
                                } else if (i == 8) {
                                    Glide.with(this@Fragment3).load(fishes[i].fishimgurl)
                                        .into(binding.img8)
                                    binding.text8.text = fishes[i].fishName
                                }
                                i++
                                Log.d("testtest", "$i")
                                Log.d("testtest", "$uri")
                            }.addOnFailureListener {
                            }
                    }
                } else {
                    Log.d("testtest", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("testtest", "get failed with ", exception)
            }
    }
}