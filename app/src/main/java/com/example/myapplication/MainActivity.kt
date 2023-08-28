package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.FishingContent.FishingContent
import com.example.myapplication.FishingContent.Newbie
import com.example.myapplication.FishingContent.model.FishContest
import com.example.myapplication.community.HomeActivity
import com.example.myapplication.community.PostDataModel
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.kdy.FishinfoActivity
import com.example.myapplication.kdy.FishplaceActivity
import com.example.myapplication.kdy.JoinActivity
import com.example.myapplication.kdy.LoginActivity
import com.example.myapplication.kdy.adapter.CommunityAdapter
import com.example.myapplication.kdy.adapter.MainAdapter
import com.example.myapplication.kdy.adapter.MainbanerAdapter
import com.example.myapplication.kdy.adapter.PlaceAdapter
import com.example.myapplication.weather_imgfind.findfish.FindFishActivity
import com.example.myapplication.weather_imgfind.weather.MapActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
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

        findViewById<ImageView>(R.id.logomain2).setOnClickListener {
            val intent = Intent(this@MainActivity, MainActivity::class.java)
            startActivity(intent)
        }

        // 네비게이션바 페이지 이동
        findViewById<ImageView>(R.id.homepage).setOnClickListener{
            val intent = Intent(this@MainActivity, MainActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.weatherpage).setOnClickListener{
            val intent = Intent(this@MainActivity, MapActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.cumunitypage).setOnClickListener{
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.mypage).setOnClickListener{
            val intent = Intent(this@MainActivity, MypageActivity::class.java)
            startActivity(intent)
        }

        // sharedPreference에서 데이터 받아와서 이름/프로필사진 띄움
        val sharedPref = getSharedPreferences("logininfo", Context.MODE_PRIVATE)
        val nick = sharedPref.getString("nickname", "")
        val url = sharedPref.getString("profileuri", "")
        findViewById<TextView>(R.id.toolbarnick2).text = nick
        if(url != "") {
            Glide.with(this)
                .load(url)
                .into(findViewById(R.id.toolbarprofile2))
        }

        binding.viewpagerbaner.adapter = MainbanerAdapter(this)

        binding.mainDrawerView.setNavigationItemSelectedListener { it ->
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

        //setSupportActionBar(binding.toolbar)


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

        binding.weatherforfishing.setOnClickListener {
            val intent = Intent(this@MainActivity, MapActivity::class.java)
            startActivity(intent)
        }

        binding.fishinfo.setOnClickListener {
            val intent = Intent(this@MainActivity, FishinfoActivity::class.java)
            startActivity(intent)
        }

        binding.fishplace.setOnClickListener {
            val intent = Intent(this@MainActivity, FishplaceActivity::class.java)
            startActivity(intent)
        }

        binding.fishtonement.setOnClickListener {
            val intent = Intent(this@MainActivity, FishingContent::class.java)
            startActivity(intent)
        }

        val db = Firebase.firestore.collection("BoardPosts")
        var imgcnt = 0
        var totalcnt = 0
        var urilist = mutableListOf<String>()
        val imgs = db.get()
            .addOnSuccessListener { doc ->
                if(doc != null) {
                    val myimg = doc.documents
                    var imgcount = 0
                    myimg.forEach{
                        if(it.exists()) {
                            val img = ((it.data?.getValue("Posts") as HashMap<String, String>))
                                .getValue("pictures") as MutableList<String>
                            totalcnt++
                            if(imgcnt < 5) {
                                urilist.add(img.get(0))
                                imgcnt++
                        }
                            if(doc.size() == totalcnt) {
                                binding.commnuityrecyclerview.adapter = CommunityAdapter(urilist)
                                val linearLayoutManager = LinearLayoutManager(this)
                                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                                binding.commnuityrecyclerview.layoutManager = linearLayoutManager
                            }

                    }
                    }
                }
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