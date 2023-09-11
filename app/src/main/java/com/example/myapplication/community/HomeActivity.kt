package com.example.myapplication.community

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.MainActivity
import com.example.myapplication.MypageActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBoardBinding
import com.example.myapplication.kdy.LoginActivity
import com.example.myapplication.weather_imgfind.weather.MapActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBoardBinding

    private var mAdapter: AdapterBoard? = null
    private var pList: ArrayList<PostDataModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        findViewById<ImageView>(R.id.logomain).setOnClickListener{
            val intent = Intent(this@HomeActivity, MainActivity::class.java)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.backbtn).setOnClickListener { finish() }
        findViewById<TextView>(R.id.activitytitle).text = "커뮤니티"
        val sharedPref = getSharedPreferences("logininfo", Context.MODE_PRIVATE)
        val nick = sharedPref.getString("nickname", "")
        val url = sharedPref.getString("profileuri", "")
        val logincheck = sharedPref.getBoolean("signedup", false)
        if(logincheck) {
            findViewById<TextView>(R.id.toolbarnick).text = nick
            if(url != "") {
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
            val intent = Intent(this@HomeActivity, MainActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.weatherpage).setOnClickListener{
            val intent = Intent(this@HomeActivity, MapActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.cumunitypage).setOnClickListener{
            val intent = Intent(this@HomeActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.mypage).setOnClickListener{
            val intent = Intent(this@HomeActivity, MypageActivity::class.java)
            startActivity(intent)
        }
        initVariable()
        getPosts()
        onViewClick()

    }

    private fun initVariable() {
        //presenter 싱글톤 사용을 위해서 getInstance() 최초 한번 호출
        pList = java.util.ArrayList()
        mAdapter = AdapterBoard(pList!!)
        binding.rePosts.layoutManager = LinearLayoutManager(this)
        binding.rePosts.adapter = mAdapter
    }

    private fun getPosts() {
        PresenterPost.instance?.getPost(object : PresenterPost.IPostsResultCallback {
            override fun onResult(list: ArrayList<PostDataModel>?) {
                Log.i("##INFO", "list.size = ${list?.size}: ")
                Log.i("##INFO", "list.size = ${list}: ")
                pList = list
                mAdapter!!.updatePostList(list!!)
                binding.rePosts.adapter = mAdapter
            }
            override fun onError(erMsg: String?) {
                Log.e("##H", "onError: error = $erMsg")
            }
        })
    }

    private fun onViewClick() {
        findViewById<FloatingActionButton>(R.id.im_write_main_board).setOnClickListener {
            Log.d("aaa", "clicked")
            startActivity(Intent(this, ActivityWritePost::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        getPosts()
    }

}