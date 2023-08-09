package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.kdy.FishinfoActivity
import com.example.myapplication.kdy.FishplaceActivity
import com.example.myapplication.kdy.LoginActivity
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

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
}