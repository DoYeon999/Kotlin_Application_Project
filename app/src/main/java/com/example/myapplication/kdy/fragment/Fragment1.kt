package com.example.myapplication.kdy.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.myapplication.FishingContent.FishService
import com.example.myapplication.R
import com.example.myapplication.databinding.Fragment1Binding
import com.example.myapplication.kdy.APIApplication
import com.example.myapplication.kdy.FishpageActivity
import com.example.myapplication.kdy.model.FishInfoModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Fragment1 : Fragment(), View.OnClickListener  {

    lateinit var binding: Fragment1Binding
    lateinit var fishService : FishService
    lateinit var fishsublist : List<FishInfoModel>

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


        val application = APIApplication()
        fishService = application.fishService
        val fishdb = fishService.getFishList()
        fishdb.enqueue(object : Callback<List<FishInfoModel>> {
            override fun onResponse(call: Call<List<FishInfoModel>>, response: Response<List<FishInfoModel>>) {
                val fishlist = response.body()
                fishsublist = fishlist?.subList(0, 9)!!
                for(i in 0 until 9) {
                    if (i == 0) {
                        Glide.with(this@Fragment1).load(fishsublist?.get(i)?.imgurl)
                            .into(binding.img0)
                        binding.text0.text = fishsublist?.get(i)?.fishname
                    } else if (i == 1) {
                        Glide.with(this@Fragment1).load(fishsublist?.get(i)?.imgurl)
                            .into(binding.img1)
                        binding.text1.text = fishsublist?.get(i)?.fishname
                    } else if (i == 2) {
                        Glide.with(this@Fragment1).load(fishsublist?.get(i)?.imgurl)
                            .into(binding.img2)
                        binding.text2.text = fishsublist?.get(i)?.fishname
                    } else if (i == 3) {
                        Glide.with(this@Fragment1).load(fishsublist?.get(i)?.imgurl)
                            .into(binding.img3)
                        binding.text3.text = fishsublist?.get(i)?.fishname
                    } else if (i == 4) {
                        Glide.with(this@Fragment1).load(fishsublist?.get(i)?.imgurl)
                            .into(binding.img4)
                        binding.text4.text = fishsublist?.get(i)?.fishname
                    } else if (i == 5) {
                        Glide.with(this@Fragment1).load(fishsublist?.get(i)?.imgurl)
                            .into(binding.img5)
                        binding.text5.text = fishsublist?.get(i)?.fishname
                    } else if (i == 6) {
                        Glide.with(this@Fragment1).load(fishsublist?.get(i)?.imgurl)
                            .into(binding.img6)
                        binding.text6.text = fishsublist?.get(i)?.fishname
                    } else if (i == 7) {
                        Glide.with(this@Fragment1).load(fishsublist?.get(i)?.imgurl)
                            .into(binding.img7)
                        binding.text7.text = fishsublist?.get(i)?.fishname
                    } else if (i == 8) {
                        Glide.with(this@Fragment1).load(fishsublist?.get(i)?.imgurl)
                            .into(binding.img8)
                        binding.text8.text = fishsublist?.get(i)?.fishname
                    }
                }
            }

            override fun onFailure(call: Call<List<FishInfoModel>>, t: Throwable) {
                Log.d("test1234", "failed")
            }
        })
        Log.d("testtest", "xx")
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
                val fish = fishsublist.get(0)
                val intent = Intent(activity, FishpageActivity::class.java)
                intent.putExtra("fish", fish)
                startActivity(intent)
            }

            R.id.img1 -> {
                Log.d("test","test clicked..")
                val fish = fishsublist.get(1)
                val intent = Intent(activity, FishpageActivity::class.java)
                intent.putExtra("fish", fish)
                startActivity(intent)
            }

            R.id.img2 -> {
                Log.d("test","test clicked..")
                val fish = fishsublist.get(2)
                val intent = Intent(activity, FishpageActivity::class.java)
                intent.putExtra("fish", fish)
                startActivity(intent)
            }

            R.id.img3 -> {
                Log.d("test","test clicked..")
                val fish = fishsublist.get(3)
                val intent = Intent(activity, FishpageActivity::class.java)
                intent.putExtra("fish", fish)
                startActivity(intent)
            }

            R.id.img4 -> {
                Log.d("test","test clicked..")
                val fish = fishsublist.get(4)
                val intent = Intent(activity, FishpageActivity::class.java)
                intent.putExtra("fish", fish)
                startActivity(intent)
            }

            R.id.img5 -> {
                Log.d("test","test clicked..")
                val fish = fishsublist.get(5)
                val intent = Intent(activity, FishpageActivity::class.java)
                intent.putExtra("fish", fish)
                startActivity(intent)
            }

            R.id.img6 -> {
                Log.d("test","test clicked..")
                val fish = fishsublist.get(6)
                val intent = Intent(activity, FishpageActivity::class.java)
                intent.putExtra("fish", fish)
                startActivity(intent)
            }

            R.id.img7 -> {
                Log.d("test","test clicked..")
                val fish = fishsublist.get(7)
                val intent = Intent(activity, FishpageActivity::class.java)
                intent.putExtra("fish", fish)
                startActivity(intent)
            }

            R.id.img8 -> {
                Log.d("test","test clicked..")
                val fish = fishsublist.get(8)
                val intent = Intent(activity, FishpageActivity::class.java)
                intent.putExtra("fish", fish)
                startActivity(intent)
            }
        }
    }


}