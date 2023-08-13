//package com.example.myapplication.community
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.myapplication.databinding.ActivityMainBoardBinding
//
//class HomeActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityMainBoardBinding
//
//    private var mAdapter: AdapterBoard? = null
//    private var pList: ArrayList<PostDataModel>? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBoardBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        initVariable()
//        getPosts()
//        onViewClick()
//
//    }
//
//    private fun initVariable() {
//        //presenter 싱글톤 사용을 위해서 getInstance() 최초 한번 호출
//        pList = java.util.ArrayList()
//        mAdapter = AdapterBoard(pList!!)
//        binding.rePosts.layoutManager = LinearLayoutManager(this)
//        binding.rePosts.adapter = mAdapter
//    }
//
//    private fun getPosts() {
//        PresenterPost.instance?.getPost(object : PresenterPost.IPostsResultCallback {
//            override fun onResult(list: ArrayList<PostDataModel>?) {
//                Log.i("##INFO", "list.size = ${list?.size}: ")
//                Log.i("##INFO", "list.size = ${list}: ")
//                pList = list
//                mAdapter!!.updatePostList(list!!)
//                binding.rePosts.adapter = mAdapter
//            }
//
//            override fun onError(erMsg: String?) {
//                Log.e("##H", "onError: error = $erMsg")
//            }
//        })
//    }
//
//    private fun onViewClick() {
//        binding.imWriteMainBoard.setOnClickListener {
//            startActivity(Intent(this, ActivityWritePost::class.java))
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        getPosts()
//    }
//
//}