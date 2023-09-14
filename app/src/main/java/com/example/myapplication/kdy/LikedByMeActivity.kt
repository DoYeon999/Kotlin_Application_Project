package com.example.myapplication.kdy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.MainActivity
import com.example.myapplication.MypageActivity
import com.example.myapplication.R
import com.example.myapplication.community.ActivityWritePost
import com.example.myapplication.community.AdapterBoard
import com.example.myapplication.community.HomeActivity
import com.example.myapplication.community.PostDataModel
import com.example.myapplication.community.Replies
import com.example.myapplication.databinding.ActivityLikedByMeBinding
import com.example.myapplication.weather_imgfind.weather.MapActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class LikedByMeActivity : AppCompatActivity() {

    private var mAdapter: AdapterBoard? = null
    private var pList: ArrayList<PostDataModel>? = null

    lateinit var binding : ActivityLikedByMeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLikedByMeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        findViewById<ImageView>(R.id.logomain).setOnClickListener{
            val intent = Intent(this@LikedByMeActivity, MainActivity::class.java)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.backbtn).setOnClickListener { finish() }
        findViewById<TextView>(R.id.activitytitle).text = "게시글 찜목록"
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
            val intent = Intent(this@LikedByMeActivity, MainActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.weatherpage).setOnClickListener{
            val intent = Intent(this@LikedByMeActivity, MapActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.cumunitypage).setOnClickListener{
            val intent = Intent(this@LikedByMeActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.mypage).setOnClickListener{
            val intent = Intent(this@LikedByMeActivity, MypageActivity::class.java)
            startActivity(intent)
        }
        initVariable()
        var count = 0
        val db = FirebaseFirestore.getInstance()
        db.collection("BoardPosts").get().addOnSuccessListener { queryDocumentSnapshots ->
            if (!queryDocumentSnapshots.isEmpty) {
                for (snapshot in queryDocumentSnapshots) {
                    //getId() = documentId를 가져온다
                    var res: HashMap<String?, PostDataModel?>? =
                        HashMap<String?, PostDataModel?>()
                    res = snapshot["Posts"] as HashMap<String?, PostDataModel?>?
                    if (res != null) {
                        val data = PostDataModel()
                        data.nickname = java.lang.String.valueOf(res["nickname"])
                        Log.d("mycomm", "${data.nickname}, **** ${sharedPref.getString("id", "")}")
                        val likelist = res.get("favorites") as HashMap<String, Boolean>
                        likelist.forEach {
                            if(it.key == sharedPref.getString("id", "")) {
                                val data = PostDataModel()
                                data.id = snapshot.id
                                data.fishspecies = java.lang.String.valueOf(res["fishspecies"])
                                data.content = java.lang.String.valueOf(res["content"])
                                data.replies = ArrayList<Replies>(res["replies"] as Collection<Replies>?)
                                data.pictures = ArrayList(res["pictures"] as Collection<String>?)
                                data.favorites = res.get("favorites") as HashMap<String, Boolean>
                                data.favoriteCount = java.lang.String.valueOf(res.get("favoriteCount")).toInt()
                                data.wherecatchfish = java.lang.String.valueOf(res.get("wherecatchfish"))
                                data.writerProfile = java.lang.String.valueOf(res.get("writerProfile"))
                                pList?.add(data)
                                return@forEach
                            }
                        }
                    }
                    count++
                    if(count == queryDocumentSnapshots.size()) {
                        binding.rePosts.adapter = AdapterBoard(pList!!)
                    }
                }
            }
        }
        onViewClick()
    }

    private fun initVariable() {
        //presenter 싱글톤 사용을 위해서 getInstance() 최초 한번 호출
        pList = java.util.ArrayList()
        mAdapter = AdapterBoard(pList!!)
        binding.rePosts.layoutManager = LinearLayoutManager(this)
        binding.rePosts.adapter = mAdapter
    }


    private fun onViewClick() {
        findViewById<FloatingActionButton>(R.id.im_write_main_board).setOnClickListener {
            Log.d("aaa", "clicked")
            startActivity(Intent(this, ActivityWritePost::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val db = FirebaseFirestore.getInstance()
        val sharedPref = getSharedPreferences("logininfo", Context.MODE_PRIVATE)
        db.collection("BoardPosts").get().addOnSuccessListener { queryDocumentSnapshots ->
            val postsList: ArrayList<PostDataModel> = ArrayList<PostDataModel>()
            if (!queryDocumentSnapshots.isEmpty) {
                for (snapshot in queryDocumentSnapshots) {
                    //getId() = documentId를 가져온다
                    var res: HashMap<String?, PostDataModel?>? =
                        HashMap<String?, PostDataModel?>()
                    res = snapshot["Posts"] as HashMap<String?, PostDataModel?>?
                    if (res != null) {
                        val data = PostDataModel()
                        data.nickname = java.lang.String.valueOf(res["nickname"])
                        if(data.nickname == sharedPref.getString("id", "")) {
                            data.id = snapshot.id
                            data.fishspecies = java.lang.String.valueOf(res["fishspecies"])
                            data.content = java.lang.String.valueOf(res["content"])
                            data.replies = ArrayList<Replies>(res["replies"] as Collection<Replies>?)
                            data.pictures = ArrayList(res["pictures"] as Collection<String>?)
                            data.favorites = res.get("favorites") as HashMap<String, Boolean>
                            data.favoriteCount =
                                java.lang.String.valueOf(res.get("favoriteCount")).toInt()
                            data.wherecatchfish = java.lang.String.valueOf(res.get("wherecatchfish"))
                            data.writerProfile = java.lang.String.valueOf(res.get("writerProfile"))
                            postsList.add(data)
                        }
                    }
                }
            }
        }
    }
}