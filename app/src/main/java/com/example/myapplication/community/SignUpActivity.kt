//package com.example.myapplication.community
//
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.myapplication.databinding.ActivitySingUpBinding
//import com.google.android.gms.tasks.Task
//import com.google.firebase.auth.AuthResult
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//
//class SignUpActivity : AppCompatActivity() {
//    private lateinit var binding: ActivitySingUpBinding
//    var firebaseAuth: FirebaseAuth? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivitySingUpBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        firebaseAuth = FirebaseAuth.getInstance()
//        itemClick()
//    }
//
//    private fun itemClick() {
//        // 회원가입 버튼
//        binding.btJoin.setOnClickListener {
//            if (!binding.edEmail.text.toString().equals("")
//                && !binding.edPassword.text.toString().equals("")
//                && !binding.edNickname.text.toString().equals("")
//            ) {
//                // 이메일과 비밀번호가 공백이 아닌 경우
//                createUser(binding.edEmail.text.toString(), binding.edPassword.text.toString(), binding.edNickname.text.toString())
//            } else {
//                // 이메일과 비밀번호가 공백인 경우
//                Toast.makeText(this@SignUpActivity, "모든 입력란을 작성해주세요.", Toast.LENGTH_LONG).show()
//            }
//        }
//
//        binding.imBack.setOnClickListener{
//            finish()
//        }
//    }
//
//    //유저의 데이터를 전송하여 회원가입을 진행한다.
//    private fun createUser(email: String, password: String, nickname: String) {
//        firebaseAuth!!.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task: Task<AuthResult?> ->
//                if (task.isSuccessful) {
//                    val userModel = UserModel()
//                    val uid = FirebaseAuth.getInstance().currentUser!!.uid
//                    userModel.email = email
//                    userModel.password = password
//                    userModel.nickname = nickname
//                    val db = FirebaseFirestore.getInstance()
//                    db.collection("Users").document(uid).set(userModel)
//                        .addOnSuccessListener { aVoid: Void? ->
//                            Toast.makeText(this, "회원가입 성공", Toast.LENGTH_LONG).show()
//                            finish()
//                        }.addOnFailureListener { e: Exception ->
//                            Log.e(
//                                "SignUpActivity",
//                                "onFailure: " + e.message
//                            )
//                        }
//
//                } else {
//                    task.addOnFailureListener {
//                        Log.e("##ERROR", ": error = task erro = ${it.message}");
//                    }
//                    Toast.makeText(this, "회원가입 실패", Toast.LENGTH_LONG).show()
//                }
//            }
//    }
//}