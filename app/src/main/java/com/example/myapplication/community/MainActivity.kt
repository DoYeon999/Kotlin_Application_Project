package com.example.myapplication.community

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        itemClick()

    }


    private fun itemClick() {
        // 로그인 버튼 클릭시 파이어베이스 auth로 유저의 정보를 전송한다.
        binding.btLogin.setOnClickListener { view ->

            val auth: FirebaseAuth
            auth = FirebaseAuth.getInstance()
            val email: String = binding.edEmail.getText().toString()
            val passward: String = binding.edPassword.getText().toString()
            auth.signInWithEmailAndPassword(email, passward)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                    } else {
                        Log.i(
                            "##INFO",
                            "onComplete(): failure",
                            task.exception
                        )
                    }
                    task.addOnFailureListener { e: Exception ->
                        Toast.makeText(this@MainActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                        Log.i("##INFO", "onComplete(): e = " + e.message)
                    }
                }

                .addOnFailureListener {
                    Log.e("##ERROR", "it = ${it.message} ");
                }
                .addOnSuccessListener {
                    Toast.makeText(this@MainActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                    getUserInfoToDataBase()

                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    startActivity(intent)
                }
        }

        //회원가입 페이지로 이동하는 클릭 이벤트 리스너
        binding.btSignup.setOnClickListener { view ->
            val intent = Intent(this@MainActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    //유저가 로그인 했을때 유저의 데이터를 가져오는 부분
    private fun getUserInfoToDataBase() {
        val db = FirebaseFirestore.getInstance()
        val id = FirebaseAuth.getInstance().currentUser?.uid
        if (id != null) {
            db.collection("Users").document(id).get().addOnSuccessListener { queryDocumentSnapshots ->
                val userInfo = queryDocumentSnapshots.toObject(UserModel::class.java)
                if (userInfo != null) {
                }
            }.addOnFailureListener { e ->
                Log.d(TAG, "onFailure: " + e.message)
            }
        } else {
            Log.i("##INFO", "uid is null");
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val RC_SIGN_IN = 9001
    }
}