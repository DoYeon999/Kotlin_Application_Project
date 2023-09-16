package com.example.myapplication.FishingContent

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.myapplication.FishingContent.model.FishBait
import com.example.myapplication.FishingContent.model.FishFish
import com.example.myapplication.FishingContent.model.FishRope
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityNewbieBinding
import com.example.myapplication.FishingContent.model.Guide
import com.example.myapplication.FishingContent.recycler.NewbieAdpater
import com.example.myapplication.FishingContent.recycler.NewbieAdpater2
import com.example.myapplication.FishingContent.recycler.NewbieAdpater3
import com.example.myapplication.FishingContent.recycler.PhDividerItemDecoration
import com.example.myapplication.MainActivity
import com.example.myapplication.MypageActivity
import com.example.myapplication.community.HomeActivity
import com.example.myapplication.kdy.LoginActivity
import com.example.myapplication.weather_imgfind.net.APIApplication
import com.example.myapplication.weather_imgfind.weather.MapActivity
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Newbie : AppCompatActivity() {
    private lateinit var binding: ActivityNewbieBinding
    private var newbielist = mutableListOf<Guide>()
    lateinit var nbAdapter : NewbieAdpater
    lateinit var nbAdapter2 : NewbieAdpater2
    lateinit var nbAdapter3 : NewbieAdpater3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewbieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFirestoreData("매듭")
        findViewById<TextView>(R.id.loginbuttonmain).setOnClickListener {
            val intent = Intent(this@Newbie, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener {
            newbielist.clear()
            loadFirestoreData("매듭")  // 원하는 문서 ID를 적절히 변경하세요
        }

        binding.button1.setOnClickListener {
            newbielist.clear()
            loadFirestoreData("미끼")  // 원하는 문서 ID를 적절히 변경하세요
        }

        binding.button2.setOnClickListener {
            newbielist.clear()
            loadFirestoreData("회")  // 원하는 문서 ID를 적절히 변경하세요
        }

        findViewById<ImageView>(R.id.logomain).setOnClickListener{
            val intent = Intent(this@Newbie, MainActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.backbtn).setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.activitytitle).text = "초보자 가이드"
        val sharedPref = getSharedPreferences("logininfo", Context.MODE_PRIVATE)
        val nick = sharedPref.getString("nickname", "")
        val url = sharedPref.getString("profileuri", "")
        val logincheck = sharedPref.getBoolean("signedup", false)
        if(logincheck) {
            findViewById<TextView>(R.id.toolbarnick).text = nick
            if(url != "" && url != "null") {
                Glide.with(this)
                    .load(url)
                    .into(findViewById(R.id.toolbarprofile))
            }
            findViewById<TextView>(R.id.loginbuttonmain).visibility = View.GONE
            findViewById<TextView>(R.id.toolbarnick).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.toolbarprofile).visibility = View.VISIBLE
        }

        // 네비게이션바 페이지 이동
        findViewById<ImageView>(R.id.homepage).setOnClickListener{
            val intent = Intent(this@Newbie, MainActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.weatherpage).setOnClickListener{
            val intent = Intent(this@Newbie, MapActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.cumunitypage).setOnClickListener{
            if(logincheck) {
                val intent = Intent(this@Newbie, HomeActivity::class.java)
                startActivity(intent)
            } else {
                binding.newbielayout.alpha = 0.2f
                val dialog = AlertDialog.Builder(this).run {
                    setMessage("로그인한 사용자만 이용할 수 있는 기능입니다.")
                        .setPositiveButton("로그인하기") { it, now ->
                            val intent = Intent(this@Newbie, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        .setNegativeButton("취소") { it, now ->
                            it.dismiss()
                            //val opacity = ContextCompat.getColor(this@MainActivity, R.color.opacity_100)
                            binding.newbielayout.alpha = 1.0f
                        }
                }
                dialog.setCancelable(false)
                dialog.show()
            }
        }

        findViewById<ImageView>(R.id.mypage).setOnClickListener{
            if(logincheck) {
                val intent = Intent(this@Newbie, MypageActivity::class.java)
                startActivity(intent)
            } else {
                binding.newbielayout.alpha = 0.2f
                val dialog = AlertDialog.Builder(this).run {
                    setMessage("로그인한 사용자만 이용할 수 있는 기능입니다.")
                        .setPositiveButton("로그인하기") { it, now ->
                            val intent = Intent(this@Newbie, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        .setNegativeButton("취소") { it, now ->
                            it.dismiss()
                            //val opacity = ContextCompat.getColor(this@MainActivity, R.color.opacity_100)
                            binding.newbielayout.alpha = 1.0f
                        }
                }
                dialog.setCancelable(false)
                dialog.show()
            }
        }
    }

    private fun loadFirestoreData(path: String) {
        Log.d("testtest1234", "asdfasdfas111111111dfasdfasfasd")
        val fishService = (applicationContext as APIApplication).fishService
        if(path == "매듭") {
            Log.d("testtest1234", "asdf15135135135asdfasdfasdfasfasd")
            val ropeService = fishService.fishRopeList()
            ropeService.enqueue(object : Callback<List<FishRope>> {
                override fun onResponse(call: Call<List<FishRope>>, response: Response<List<FishRope>>
                ) {
                    val rope = response.body()
                    Log.d("testtest1234", "$rope")
                    viewBindingFunc(rope!!)
                }

                override fun onFailure(call: Call<List<FishRope>>, t: Throwable) {
                    Log.d("testtest1234", "11366")
                }
            })
        }

        if(path == "미끼") {
            val baitService = fishService.fishBaitList()
            baitService.enqueue(object : Callback<List<FishBait>> {
                override fun onResponse(call: Call<List<FishBait>>, response: Response<List<FishBait>>
                ) {
                    val rope = response.body()
                    Log.d("testtest1234", "$rope")
                    viewBindingFunc2(rope!!)
                }

                override fun onFailure(call: Call<List<FishBait>>, t: Throwable) {

                }
            })
        }

        if(path == "회") {
            val fishFishService = fishService.fishFishList()
            fishFishService.enqueue(object : Callback<List<FishFish>> {
                override fun onResponse(call: Call<List<FishFish>>, response: Response<List<FishFish>>
                ) {
                    val rope = response.body()
                    Log.d("testtest1234", "***********$rope")
                    viewBindingFunc3(rope!!)
                }

                override fun onFailure(call: Call<List<FishFish>>, t: Throwable) {

                }
            })
        }

//        db?.collection(path)?.get()
//            ?.addOnSuccessListener {  querySnapshot  ->
//                val dataToShow = StringBuilder()
//                for (documentSnapshot in querySnapshot) {
//                    if (documentSnapshot.exists()) {
//                        val data = documentSnapshot.data
//                        val title = data?.get("title") as String // 필드 이름을 적절히 변경하세요
//                        val url = data?.get("url") as String
//                        val thumbnail = data?.get("thumbnail") as String
//                        Log.d("tt", "$title, $url, $thumbnail")
//                        newbielist.add(Guide(title, url, thumbnail))
//                        //dataToShow.append(info).append("\n")
//                    }
//                }

                /*
                if (dataToShow.isNotEmpty()) {
                    binding.textView.text = dataToShow.toString()
                } else {
                    binding.textView.text = "데이터가 없습니다."
                }*/
//            }
//            ?.addOnFailureListener { exception ->
//                //binding.textView.text = "데이터를 불러오는 중에 오류가 발생했습니다."
//                // 오류 처리 코드를 여기에 추가하세요
//            }

    }
    fun viewBindingFunc(newbies : List<FishRope>) {
        Log.d("tt", "=====$newbies=====")
        nbAdapter = NewbieAdpater(newbies)
        //Log.d("tt", "=====${nbAdapter.guides.toString()}=====")
        binding.recyclerView2.adapter = nbAdapter
        val linearLayoutManager = PhDividerItemDecoration(10F, Color.BLACK)
        binding.recyclerView2.addItemDecoration(linearLayoutManager)
//        val binding = ActivityNewbieBinding.inflate(layoutInflater)
//        val recycle  = binding.recyclerView2

    }

    fun viewBindingFunc2(newbies : List<FishBait>) {
        Log.d("tt", "=====$newbies=====")
        nbAdapter2 = NewbieAdpater2(newbies)
        //Log.d("tt", "=====${nbAdapter.guides.toString()}=====")
        binding.recyclerView2.adapter = nbAdapter2
        val linearLayoutManager = PhDividerItemDecoration(10F, Color.BLACK)
        binding.recyclerView2.addItemDecoration(linearLayoutManager)
//        val binding = ActivityNewbieBinding.inflate(layoutInflater)
//        val recycle  = binding.recyclerView2

    }

    fun viewBindingFunc3(newbies : List<FishFish>) {
        Log.d("tt", "=====$newbies=====")
        nbAdapter3 = NewbieAdpater3(newbies)
        //Log.d("tt", "=====${nbAdapter.guides.toString()}=====")
        binding.recyclerView2.adapter = nbAdapter3
        val linearLayoutManager = PhDividerItemDecoration(10F, Color.BLACK)
        binding.recyclerView2.addItemDecoration(linearLayoutManager)
//        val binding = ActivityNewbieBinding.inflate(layoutInflater)
//        val recycle  = binding.recyclerView2

    }
}
