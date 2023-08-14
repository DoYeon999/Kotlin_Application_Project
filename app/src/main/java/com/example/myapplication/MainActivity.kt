package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.navercorp.nid.NaverIdLoginSDK


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
//    var user: User? = null

    data class Main(val fishname : String, val fishimg : String)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainDrawerView.setNavigationItemSelectedListener {
            it ->
            if(it.title == "날씨") {
                val intent = Intent(this@MainActivity, MapActivity::class.java)
                startActivity(intent)
            }

            else if(it.title == "낚시포인트") {
                val intent = Intent(this@MainActivity, FishplaceActivity::class.java)
                startActivity(intent)
            }

            else if(it.title == "낚시박사") {
                val intent = Intent(this@MainActivity, FishinfoActivity::class.java)
                startActivity(intent)
            }

            else if(it.title == "커뮤니티") {
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                startActivity(intent)
            }

            else if(it.title == "낚시대회") {
                val intent = Intent(this@MainActivity, FishingContent::class.java)
                startActivity(intent)
            }

            else if(it.title == "초보자가이드") {
                val intent = Intent(this@MainActivity, Newbie::class.java)
                startActivity(intent)
            }

            else if(it.title == "로그인") {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            }

            else if(it.title == "회원가입") {
                val intent = Intent(this@MainActivity, JoinActivity::class.java)
                startActivity(intent)
            }

            else if(it.title =="검색") {
                val intent = Intent(this@MainActivity, FindFishActivity::class.java)
                startActivity(intent)
            }
//
//            Toast.makeText(
//                this@MainActivity,
//                "navigation item click... ${it.title}",
//                Toast.LENGTH_SHORT
//            ).show()
            true
        }

        setSupportActionBar(binding.toolbar)


        val database = Firebase.firestore
        val docList = database.collection("8월제철")
        var mainfish = mutableListOf<Main>()
        var count = 0
        docList.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val docs = document.documents
                    var checkflag = true
                    docs.forEach {
                        if(!checkflag) return@forEach
                        if(it.exists()) {
                            Log.d("test11111", "${it.data?.get("fishimg")}")
                            Log.d("test11111", "${it.data?.get("fishname")}")
                            val storage =
                                FirebaseStorage.getInstance("gs://fishing-4f003.appspot.com")
                            val storageRef =
                                storage.reference.child(it.data?.get("fishimg").toString())
                            storageRef.downloadUrl
                                .addOnSuccessListener { uri ->
                                    mainfish.add(
                                        Main(
                                            it.data?.get("fishname").toString(),
                                            uri.toString()
                                        )
                                    )
                                    count++
                                    if (docs.size == count) {
                                        Log.d("test1234", "aaaaaaaaaaaaaaaaa")
                                        val mainAdapter = MainAdapter(mainfish)
                                        val linearLayoutManager = LinearLayoutManager(this)
                                        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                                        binding.mainfish.layoutManager = linearLayoutManager
                                        binding.mainfish.adapter = mainAdapter
                                    }
                                }
                        }
                    }
                }

            }


        //네이버 로그아웃
//        binding.naverlogout.setOnClickListener {
//            NaverIdLoginSDK.logout()
//            val intent = Intent(this@MainActivity, LoginActivity::class.java)
//            startActivity(intent)
//        }

        binding.fishinfo.setOnClickListener {
            val intent = Intent(this@MainActivity, FishinfoActivity::class.java)
            startActivity(intent)
        }

        binding.fishplace.setOnClickListener {
            val intent = Intent(this@MainActivity, FishplaceActivity::class.java)
            startActivity(intent)
        }

        //        binding.profileImage.setOnClickListener {
//            showPopup(binding.profileImage)
//        }


    }

//    val requestLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult())
//    {
//        Log.d("ddd", "${it.data?.getStringExtra("updateprofileid")}")
//        Glide
//            .with(this@MainActivity)
//            .load(Uri.parse(it.data?.getStringExtra("updateprofileid")))
//            .apply(RequestOptions().override(250, 200))
//            .centerCrop()
//            .into(binding.profileImage)
//        binding.nickname.text = it.data?.getStringExtra("updatenickname")
//    }
//    override fun onMenuItemClick(item: MenuItem?): Boolean {
//        when (item?.itemId) { // 메뉴 아이템에 따라 동작 다르게 하기
//            R.id.menu_logout ->
//            {
//                val intent = Intent(this@MainActivity, LoginActivity::class.java)
//                startActivity(intent)
//                Toast.makeText(this, "로그아웃되었습니다.", Toast.LENGTH_LONG).show()
//            }
//        }
//
//        return item != null // 아이템이 null이 아닌 경우 true, null인 경우 false 리턴
//    }
//
//    private fun showPopup(v: View) {
//        val popup = PopupMenu(this, v) // PopupMenu 객체 선언
//        popup.menuInflater.inflate(R.menu.profile_menu, popup.menu) // 메뉴 레이아웃 inflate
//        popup.setOnMenuItemClickListener(this)
//        popup.show() // 팝업 보여주기
//    }
}