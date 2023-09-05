package com.example.myapplication.kdy

import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.MainActivity
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
                        if(id == tempuser?.get("id") && pw == tempuser?.get("password")) {
                            val sharedPref = getSharedPreferences("logininfo", MODE_PRIVATE)
                            sharedPref.edit().run {
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

//            if(canlogin) {
//                dialog("success")
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//            } else {
//                dialog("failure")
//            }

            /*
            // 쉐어드로부터 저장된 id, pw 가져오기
            val sharedPreference = getSharedPreferences("user name", Context.MODE_PRIVATE)
            val savedId = sharedPreference.getString("id", "")
            val savedPw = sharedPreference.getString("pw", "")

            // 유저가 입력한 id, pw값과 쉐어드로 불러온 id, pw값 비교
            if(id == savedId && pw == savedPw){
                // 로그인 성공 다이얼로그 보여주기
                dialog("success")

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }
            else{
                // 로그인 실패 다이얼로그 보여주기
                dialog("failure")
            }*/

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
    // 로그인 성공/실패 시 다이얼로그를 띄워주는 메소드
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