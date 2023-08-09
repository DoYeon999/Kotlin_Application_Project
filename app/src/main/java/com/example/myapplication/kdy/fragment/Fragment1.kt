package com.example.myapplication.kdy.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.Fragment1Binding
import com.example.myapplication.kdy.FishpageActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class Fragment1 : Fragment(), View.OnClickListener  {

    lateinit var binding: Fragment1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Fragment1Binding.inflate(inflater, container, false)
        return binding.root
    }

    //파이어베이스 데이터 가져오기
    data class Fish(val fishName: String, val fishimgurl: String)

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = Fragment1Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val database = Firebase.firestore
        val docList = database.collection("fishing")
        Log.d("testtest", "xx")

            var fishes = mutableListOf<Fish>()
            docList.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val docs = document.documents
                        var i = 0
                        docs.forEach {
                            Log.d("test11111", "${it.data?.get("fishName")}")
                            Log.d("test11111", "${it.data?.get("fishimgurl")}")
                            val storage =
                                FirebaseStorage.getInstance("gs://fishing-4f003.appspot.com")
                            val storageRef =
                                storage.reference.child(it.data?.get("fishimgurl").toString())
                            storageRef.downloadUrl
                                .addOnSuccessListener { uri ->
                                    Log.d("testtest", uri.toString())
                                    fishes.add(
                                        Fish(
                                            it.data?.get("fishName").toString(), uri.toString()
                                        ))
                                    Log.d("testtest", "$i ---- ${fishes.size}")
                                    if(fishes.size == 9) {
                                        Log.d("test11111", "$fishes")
                                        for(i in 0 until 9) {
                                            if (i == 0) {
                                                Glide.with(this@Fragment1).load(fishes[i].fishimgurl)
                                                    .into(binding.img0)
                                                binding.text0.text = fishes[i].fishName
                                            } else if (i == 1) {
                                                Glide.with(this@Fragment1).load(fishes[i].fishimgurl)
                                                    .into(binding.img1)
                                                binding.text1.text = fishes[i].fishName
                                            } else if (i == 2) {
                                                Glide.with(this@Fragment1).load(fishes[i].fishimgurl)
                                                    .into(binding.img2)
                                                binding.text2.text = fishes[i].fishName
                                            } else if (i == 3) {
                                                Glide.with(this@Fragment1).load(fishes[i].fishimgurl)
                                                    .into(binding.img3)
                                                binding.text3.text = fishes[i].fishName
                                            } else if (i == 4) {
                                                Glide.with(this@Fragment1).load(fishes[i].fishimgurl)
                                                    .into(binding.img4)
                                                binding.text4.text = fishes[i].fishName
                                            } else if (i == 5) {
                                                Glide.with(this@Fragment1).load(fishes[i].fishimgurl)
                                                    .into(binding.img5)
                                                binding.text5.text = fishes[i].fishName
                                            } else if (i == 6) {
                                                Glide.with(this@Fragment1).load(fishes[i].fishimgurl)
                                                    .into(binding.img6)
                                                binding.text6.text = fishes[i].fishName
                                            } else if (i == 7) {
                                                Glide.with(this@Fragment1).load(fishes[i].fishimgurl)
                                                    .into(binding.img7)
                                                binding.text7.text = fishes[i].fishName
                                            } else if (i == 8) {
                                                Glide.with(this@Fragment1).load(fishes[i].fishimgurl)
                                                    .into(binding.img8)
                                                binding.text8.text = fishes[i].fishName
                                            }
                                        }
                                    }
                                    i++
                                    Log.d("testtest", "$i")
                                    Log.d("testtest", "$uri")
                                }.addOnFailureListener {}
                        }
                    } else {
                        Log.d("testtest", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("testtest", "get failed with ", exception)
                }

    }

    //setOnClickListener
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    //상세 페이지 이동
    private fun setOnClickListener() {
        binding.img0.setOnClickListener(this)
        binding.img1.setOnClickListener(this)
        binding.img2.setOnClickListener(this)
        binding.img3.setOnClickListener(this)
        binding.img4.setOnClickListener(this)
        binding.img5.setOnClickListener(this)
        binding.img6.setOnClickListener(this)
        binding.img7.setOnClickListener(this)
        binding.img8.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.img0 -> {
                Log.d("test","test clicked..")
                val intent = Intent(activity, FishpageActivity::class.java)
                startActivity(intent)
            }

            R.id.img1 -> {
                Log.d("test","test clicked..")
                val intent = Intent(activity, FishpageActivity::class.java)
                startActivity(intent)
            }

            R.id.img2 -> {
                Log.d("test","test clicked..")
                val intent = Intent(activity, FishpageActivity::class.java)
                startActivity(intent)
            }

            R.id.img3 -> {
                Log.d("test","test clicked..")
                val intent = Intent(activity, FishpageActivity::class.java)
                startActivity(intent)
            }

            R.id.img4 -> {
                Log.d("test","test clicked..")
                val intent = Intent(activity, FishpageActivity::class.java)
                startActivity(intent)
            }

            R.id.img5 -> {
                Log.d("test","test clicked..")
                val intent = Intent(activity, FishpageActivity::class.java)
                startActivity(intent)
            }

            R.id.img6 -> {
                Log.d("test","test clicked..")
                val intent = Intent(activity, FishpageActivity::class.java)
                startActivity(intent)
            }

            R.id.img7 -> {
                Log.d("test","test clicked..")
                val intent = Intent(activity, FishpageActivity::class.java)
                startActivity(intent)
            }

            R.id.img8 -> {
                Log.d("test","test clicked..")
                val intent = Intent(activity, FishpageActivity::class.java)
                startActivity(intent)
            }
        }
    }


}