package com.example.myapplication.kdy

import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityLoginBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val binding = ActivityLoginBinding.inflate(layoutInflater)

        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                Log.d("naver", "${NaverIdLoginSDK.getAccessToken()}")
                Log.d("naver", "${NaverIdLoginSDK.getRefreshToken()}")
                Log.d("naver", "${NaverIdLoginSDK.getExpiresAt().toString()}")
                Log.d("naver", "${NaverIdLoginSDK.getTokenType()}")
                Log.d("naver", "${NaverIdLoginSDK.getState().toString()}")
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                Log.d("naver", "login complete")
                startActivity(intent)
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@LoginActivity,"errorCode:$errorCode, errorDesc:$errorDescription",
                    Toast.LENGTH_SHORT).show()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        findViewById<TextView>(R.id.loginbuttonmain2).visibility = View.GONE

        // 로고 클릭 시
        findViewById<ImageView>(R.id.logomain2).setOnClickListener {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }

        //로그인 버튼 - 로그인 시에 shared preference에 로그인 정보를 넣음
        binding.loginbutton.setOnClickListener{
            val id : String = binding.idInput.text.toString()
            val pw : String = binding.pwInput.text.toString()
            //var canlogin = false
            val db = FirebaseFirestore.getInstance()
            var count = 0
            db.collection("UserInfo")
                .get()
                .addOnSuccessListener { users ->
                    for(user in users.documents) {
                        val tempuser = user.data
                        Log.d("logintest", "${user}")
                        if(id == tempuser?.get("id") && pw == tempuser?.get("password")) {
                            val sharedPref = getSharedPreferences("logininfo", MODE_PRIVATE)
                            sharedPref.edit().run {
                                putString("docid", user.id)
                                putString("id", tempuser?.get("id").toString())
                                putString("name", tempuser?.get("name").toString())
                                putString("nickname", tempuser?.get("nickname").toString())
                                putString("phone", tempuser?.get("phone").toString())
                                putString("profileuri", tempuser?.get("profileuri").toString())
                                putBoolean("signedup", true)
                                commit()
                            }
                            dialog("success")
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                            break
                        }
                        count++
                        if(users.size() == count) {
                            dialog("failure")
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "failedforlogin", Toast.LENGTH_SHORT).show()
                }

        }

        binding.signinbutton.setOnClickListener {
            val intent = Intent(this@LoginActivity, JoinActivity::class.java)
            startActivity(intent)
        }

        binding.naver.setOnClickListener {
            NaverIdLoginSDK.initialize(this@LoginActivity, "Kbfd81BNQVDUZBU07nRo", "j_ulvQXh2R","test")
            NaverIdLoginSDK.authenticate(this@LoginActivity, oauthLoginCallback)
        }

    }
    // 로그인 성공, 실패 시 다이얼로그를 띄워주는 메소드
    fun dialog(type: String){
        var dialog = AlertDialog.Builder(this)

        if(type == "success"){
            dialog.setTitle("로그인 성공")
            dialog.setMessage("로그인 성공!")
        }
        else if(type == "failure"){
            dialog.setTitle("로그인 실패")
            dialog.setMessage("아이디와 비밀번호를 확인해주세요")
        }

        var dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(ContentValues.TAG, "")
                }
            }
        }

        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }
}