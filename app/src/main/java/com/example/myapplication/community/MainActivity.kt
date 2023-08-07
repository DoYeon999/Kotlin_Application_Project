package com.android.project_board

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.circularreveal.CircularRevealHelper.Strategy
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    // 구글api클라이언트
    private var mGoogleSignInClient: GoogleSignInClient? = null

    // 구글 계정
    private var gsa: GoogleSignInAccount? = null

    // 파이어베이스 인증 객체 생성
    private var mAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        itemClick()

        // 파이어베이스 인증 객체 선언
//        mAuth = FirebaseAuth.getInstance()
    }


    private fun itemClick() {
//        binding.btLogin.setOnClickListener { view ->
//
//            val auth: FirebaseAuth
//            auth = FirebaseAuth.getInstance()
//            val email: String = binding.edEmail.getText().toString()
//            val passward: String = binding.edPassword.getText().toString()
//            auth.signInWithEmailAndPassword(email, passward)
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                    } else {
//                        Log.i(
//                            "##INFO",
//                            "onComplete(): failure",
//                            task.exception
//                        )
//                    }
//                    task.addOnFailureListener { e: Exception ->
//                        Toast.makeText(this@MainActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
//                        Log.i("##INFO", "onComplete(): e = " + e.message)
//                    }
//                }
//
//                .addOnFailureListener {
//                    Log.e("##ERROR", "it = ${it.message} ");
//                }
//                .addOnSuccessListener {
//                    Toast.makeText(this@MainActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
//                    getUserInfoToDataBase()
//
//                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
//                    startActivity(intent)
//                }
//        }
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