package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION_CODES.M
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.kdy.FishinfoActivity
import com.example.myapplication.kdy.FishplaceActivity
import com.example.myapplication.kdy.JoinActivity
import com.example.myapplication.kdy.LoginActivity
import com.google.firebase.firestore.auth.User
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
//    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainDrawerView.setNavigationItemSelectedListener {
            Toast.makeText(
                this@MainActivity,
                "navigation item click... ${it.title}",
                Toast.LENGTH_SHORT
            ).show()
            true
        }

        setSupportActionBar(binding.toolbar)

//        binding.profileImage.setOnClickListener {
//            showPopup(binding.profileImage)
//        }

        binding.naverlogout.setOnClickListener {
            NaverIdLoginSDK.logout()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
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