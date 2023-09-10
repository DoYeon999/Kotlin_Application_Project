package com.example.myapplication.kdy

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.myapplication.MypageActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityLoginModifyBinding
import com.example.myapplication.kdy.model.fish
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.internal.userAgent

class LoginModifyActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginModifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityLoginModifyBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        findViewById<TextView>(R.id.activitytitle).text = "내 정보 수정"

        // sharedPref에서 데이터 받아와서 닉네임, 프로필사진, id, pw 띄움
        val sharedPref = getSharedPreferences("logininfo", Context.MODE_PRIVATE)
        val nick = sharedPref.getString("nickname", "")
        Log.d("logintest", "$nick")
        val url = sharedPref.getString("profileuri", "")
        Log.d("logintest", "$url")
        val docId = sharedPref.getString("docid", "")
        Log.d("logintest", "$docId")
        val logincheck = sharedPref.getBoolean("signedup", false)
        if(logincheck) {
            findViewById<TextView>(R.id.toolbarnick).text = nick
            if(url != "") {
                Glide.with(this)
                    .load(url)
                    .into(findViewById(R.id.toolbarprofile))
            }
            findViewById<TextView>(R.id.loginbuttonmain).visibility = View.GONE
            findViewById<TextView>(R.id.toolbarnick).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.toolbarprofile).visibility = View.VISIBLE
        }


        val db = FirebaseFirestore.getInstance()
        var nowId = ""
        var profileuri: String? = null
        var idcheck = false
        var isExistBlank = false
        var isPWSame = false
        db.collection("UserInfo").document(docId!!)
            .get()
            .addOnSuccessListener {
                val nowUser = it.data
                if(nowUser!!.get("profileuri") != null) {
                    Glide.with(this).load(nowUser!!.get("profileuri")).into(binding.profileImage)
                    profileuri = nowUser!!.get("profileuri") as String
                }
                nowId = nowUser.get("id") as String
                binding.idInput.text = Editable.Factory.getInstance().newEditable(nowUser.get("id") as String)
                binding.pwInput.text = Editable.Factory.getInstance().newEditable(nowUser.get("password") as String)
                binding.pw2Input.text = Editable.Factory.getInstance().newEditable(nowUser.get("password") as String)
                binding.telInput.text = Editable.Factory.getInstance().newEditable(nowUser.get("phone") as String)
                binding.nicknameInput.text = Editable.Factory.getInstance().newEditable(nowUser.get("nickname") as String)
                binding.nameInput.text = Editable.Factory.getInstance().newEditable(nowUser.get("name") as String)
            }

        //binding.idInput.text

        findViewById<ImageView>(R.id.backbtn).setOnClickListener{
            val intent = Intent(this@LoginModifyActivity, MypageActivity::class.java)
            finish()
            startActivity(intent)
        }

        binding.sameidcheck.setOnClickListener {
            val id = binding.idInput.text.toString()
            if(id == nowId) {
                Toast.makeText(this, "기존 아이디와 동일합니다.", Toast.LENGTH_SHORT).show()
            }
            else {
                if(!id.isEmpty()) {
                    if (idcheck) Toast.makeText(this, "이미 확인하였습니다.", Toast.LENGTH_SHORT).show()
                    else {
                        var count = 0
                        db.collection("UserInfo").get().addOnSuccessListener {
                            val ids = it.documents
                            for (user in ids) {
                                val tempuser = user.data
                                if (tempuser?.get("id") == id) {
                                    idcheck = false
                                    break
                                }
                                count++
                                idcheck = true
                            }
                            if (count == it.size() && idcheck) {
                                Toast.makeText(this, "가능한 아이디입니다.", Toast.LENGTH_SHORT).show()
                            }
                            if (!idcheck) {
                                idcheck = false
                                Toast.makeText(this, "불가능한 아이디입니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.modifybutton.setOnClickListener {
            val id = binding.idInput.text.toString()
            val name = binding.nameInput.text.toString()
            val nickname = binding.nicknameInput.text.toString()
            val tel = binding.telInput.text.toString()
            val pw1 = binding.pwInput.text.toString()
            val pw2 = binding.pw2Input.text.toString()

            // 사용자가 필수 입력 사항을 모두 입력하지 않은 경우
            if (id.isEmpty() || name.isEmpty() || nickname.isEmpty() || tel.isEmpty() || pw1.isEmpty() || pw2.isEmpty()) {
                isExistBlank = true
            } else {
                if ((pw1).equals((pw2))) {
                    isPWSame = true
                }
            }

            Log.d("logintest", "$pw1 ,,, $pw2 ,,, ${(pw1.toString()).equals((pw2.toString()))}")
            if(id.equals(nowId)) idcheck = true
            Log.d("logintest", "$isExistBlank,,$isPWSame,,$idcheck")
            if (!isExistBlank && isPWSame && idcheck) {
                val nowDoc = FirebaseFirestore.getInstance().collection("UserInfo").document(docId!!)
                nowDoc.get().addOnSuccessListener {
                    val updateInfo = hashMapOf<String, Any>(
                        "id" to id,
                        "name" to name,
                        "nickname" to nickname,
                        "password" to pw1,
                        "phone" to tel,
                        "profileuri" to profileuri.toString()
                    )
                    nowDoc.update(updateInfo).addOnSuccessListener {
                        Toast.makeText(this@LoginModifyActivity, "정보수정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        sharedPref.edit().run {
                            putString("docid", docId)
                            putString("id", id)
                            putString("name", name)
                            putString("nickname", nickname)
                            putString("phone", tel)
                            putString("profileuri", profileuri)
                            putBoolean("signedup", true)
                            commit()
                        }
                        val intent = Intent(this@LoginModifyActivity, MypageActivity::class.java)
                        finish()
                        startActivity(intent)
                    }.addOnFailureListener {
                        Toast.makeText(this@LoginModifyActivity, "정보수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                if(isExistBlank) Toast.makeText(this@LoginModifyActivity, "빈칸이 존재합니다.", Toast.LENGTH_SHORT).show()
                else if(!isPWSame) Toast.makeText(this@LoginModifyActivity, "비밀번호가 비밀번호 확인과 다릅니다", Toast.LENGTH_SHORT).show()
                else if(idcheck) Toast.makeText(this@LoginModifyActivity, "아이디 중복확인을 진행하세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}