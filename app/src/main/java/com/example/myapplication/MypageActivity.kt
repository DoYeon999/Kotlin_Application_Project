package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.myapplication.community.HomeActivity
import com.example.myapplication.community.PostDataModel
import com.example.myapplication.community.Replies
import com.example.myapplication.databinding.ActivityMypageBinding
import com.example.myapplication.kdy.LikedByMeActivity
import com.example.myapplication.kdy.LoginActivity
import com.example.myapplication.kdy.LoginModifyActivity
import com.example.myapplication.kdy.StarredPlaceActivity
import com.example.myapplication.kdy.WrittenByMeActivity
import com.example.myapplication.weather_imgfind.weather.MapActivity
import com.google.firebase.firestore.FirebaseFirestore

class MypageActivity : AppCompatActivity() {

    lateinit var binding: ActivityMypageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityMypageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        findViewById<TextView>(R.id.activitytitle).text = "마이페이지"

        // 로고 클릭 시
        findViewById<ImageView>(R.id.logomain).setOnClickListener {
            val intent = Intent(this@MypageActivity, MainActivity::class.java)
            startActivity(intent)
        }

        // sharedPreference에서 데이터 받아와서 닉네임/프로필사진 띄움
        val sharedPref = getSharedPreferences("logininfo", Context.MODE_PRIVATE)
        val nick = sharedPref.getString("nickname", "")
        val url = sharedPref.getString("profileuri", "")
        val loginCheck = sharedPref.getBoolean("signedup", false)
        if(loginCheck) {
            findViewById<TextView>(R.id.mypagename).text = nick
            if(url != "" && url != "null") {
                Glide.with(this)
                    .load(url)
                    .into(findViewById(R.id.profileImage))
            }
            binding.logout.visibility = View.VISIBLE
            binding.memberout.visibility = View.VISIBLE
        }

        binding.logout.setOnClickListener {
            val intent = Intent(this@MypageActivity, LoginActivity::class.java)
            val sharedPref = getSharedPreferences("logininfo", MODE_PRIVATE)
            sharedPref.edit().run {
                putBoolean("signedup", false)
                commit()
            }
            startActivity(intent)
        }

        // 툴바 설정
        if(loginCheck) {
            findViewById<TextView>(R.id.loginbuttonmain).visibility = View.GONE
            findViewById<TextView>(R.id.toolbarnick).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.toolbarprofile).visibility = View.VISIBLE
            findViewById<TextView>(R.id.toolbarnick).text = nick
            if(url != "") {
                Glide.with(this)
                    .load(url)
                    .into(findViewById(R.id.toolbarprofile))
            }
        } else {
            findViewById<TextView>(R.id.loginbuttonmain).setOnClickListener {
                val intent = Intent(this@MypageActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        binding.mycommnuity.setOnClickListener {
            val intent = Intent(this@MypageActivity, WrittenByMeActivity::class.java)
            startActivity(intent)
        }

        binding.meminfomodify.setOnClickListener {
            val intent = Intent(this@MypageActivity, LoginModifyActivity::class.java)
            finish()
            startActivity(intent)
        }

        binding.staredplace.setOnClickListener {
            val intent = Intent(this@MypageActivity, StarredPlaceActivity::class.java)
            startActivity(intent)
        }

        binding.memberout.setOnClickListener {
            val docId = sharedPref.getString("docid", "")
            val dialogView = LayoutInflater.from(this).inflate(R.layout.mem_delete_check, null)
            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .setMessage("탈퇴하려면 비밀번호를 확인합니다.")
                .setPositiveButton("탈퇴") { dialog, now ->
                    val db = FirebaseFirestore.getInstance()
                    db.collection("UserInfo").document(docId!!)
                        .get()
                        .addOnSuccessListener {
                            //Log.d("deletemem", "아이디찾음")
                            //Log.d("deletemem", "${it.data!!.get("password")}")
                            val nowUser = it.data
                            val nowPW = nowUser!!.get("password").toString()
                            //Log.d("deletemem", "*** $nowPW ***")
                            val insertPW = dialogView.findViewById<EditText>(R.id.passwordChk_for_memDelete).text.toString()
                            //Log.d("deletemem", "&&& $insertPW  &&&")
                            if(insertPW == nowPW) {
                                db.collection("BoardPosts").get()
                                    .addOnSuccessListener {
                                        val docs = it.documents
                                        docs.forEach {nowDoc ->
                                            val nowdata = nowDoc.data!!.get("Posts") as HashMap<String, Any>
                                            Log.d("deletemem", "$nowdata")
                                            if(nowdata!!.get("nickname") == sharedPref.getString("id", "")) {
                                                Log.d("deletemem", "작성한 글 찾음")
                                                db.collection("BoardPosts").document(nowDoc.id).delete()
                                                    .addOnSuccessListener { Log.d("##Board", "deleted") }
                                                    .addOnFailureListener { Log.d("##Board", "not deleted") }
                                            } else {
                                                val updatePost = PostDataModel()
                                                var updated = false
                                                val likes = nowdata!!.get("favorites") as HashMap<String, Boolean>
                                                //Log.d("deletemem", "****-----$likes------*****")
                                                likes.forEach {
                                                    if(it.key == sharedPref.getString("id", "")) {
                                                        Log.d("deletemem", "좋아요 한 글 찾음")
                                                        Log.d("deletemem", "&&$nowdata$$")
                                                        updatePost.id =  nowdata!!.get("id") as String
                                                        updatePost.nickname = nowdata!!.get("nickname") as String
                                                        updatePost.replies = nowdata!!.get("replies") as ArrayList<Replies>
                                                        updatePost.pictures = nowdata!!.get("pictures") as ArrayList<String>
                                                        updatePost.content = nowdata!!.get("content") as String
                                                        updatePost.favorites = nowdata!!.get("favorites") as HashMap<String, Boolean>
                                                        updatePost.favorites.remove(sharedPref.getString("id", ""))
                                                        updatePost.favoriteCount = java.lang.String.valueOf(nowdata!!.get("favoriteCount")).toInt()
                                                        updatePost.favoriteCount -= 1
                                                        updatePost.fishspecies = nowdata!!.get("fishspecies") as String
                                                        updatePost.wherecatchfish = nowdata!!.get("wherecatchfish") as String
                                                        updatePost.writerProfile = nowdata!!.get("writerProfile") as String
                                                        updated = true
                                                        return@forEach
                                                    }
                                                }
                                                val comments = updatePost.replies
                                                Log.d("deletemem", "****-----${comments}")
                                                for(i in 0 until comments.size) {
                                                    //Log.d("deletemem", "comment for loop")
                                                    //Log.d("deletemem", "*******${comments.get(i)}*********")
                                                    updatePost.id =  nowdata!!.get("id") as String
                                                    updatePost.nickname = nowdata!!.get("nickname") as String
                                                    updatePost.replies = nowdata!!.get("replies") as ArrayList<Replies>
                                                    updatePost.pictures = nowdata!!.get("pictures") as ArrayList<String>
                                                    updatePost.content = nowdata!!.get("content") as String
                                                    updatePost.favorites = nowdata!!.get("favorites") as HashMap<String, Boolean>
                                                    updatePost.favorites.remove(sharedPref.getString("id", ""))
                                                    updatePost.favoriteCount = java.lang.String.valueOf(nowdata!!.get("favoriteCount")).toInt()
                                                    updatePost.favoriteCount -= 1
                                                    updatePost.fishspecies = nowdata!!.get("fishspecies") as String
                                                    updatePost.wherecatchfish = nowdata!!.get("wherecatchfish") as String
                                                    updatePost.writerProfile = nowdata!!.get("writerProfile") as String
                                                    val reply = comments.get(i) as HashMap<String, String>
                                                    //Log.d("deletemem", "$reply")
                                                    if(reply.get("reply_id") == sharedPref.getString("id", "")) {
                                                        Log.d("deletemem", "댓글 단 글 찾음")
                                                        comments.removeAt(i)
                                                        updatePost.replies = comments
                                                        updated = true
                                                        break
                                                    }
                                                }
                                                Log.d("deletemem", "&&&&&&&$updatePost&&&&&&&&&")
                                                if(updated) {
                                                    db.collection("BoardPosts").document(nowDoc.id).update("Posts", updatePost)
                                                        .addOnSuccessListener {Log.d("deletemem", "success delete board")}
                                                }
                                            }
                                        }
                                        db.collection("UserInfo").document(docId).delete()
                                            .addOnSuccessListener {
                                                Toast.makeText(this, "회원탈퇴에 성공했습니다.", Toast.LENGTH_SHORT).show()
                                                sharedPref.edit().run {
                                                    putBoolean("signedup", false)
                                                    commit()
                                                }
                                                val intent = Intent(this@MypageActivity, MainActivity::class.java)
                                                finish()
                                                startActivity(intent)
                                            }
                                    }
                            } else {
                                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                        }
                }
                .setNegativeButton("취소") { dialog, now ->
                    dialog.dismiss()
                }.create()
            dialog.show()
        }

        binding.likedcommunity.setOnClickListener {
            val intent = Intent(this@MypageActivity, LikedByMeActivity::class.java)
            startActivity(intent)
        }

        // 네비게이션바 페이지 이동
        findViewById<ImageView>(R.id.homepage).setOnClickListener{
            val intent = Intent(this@MypageActivity, MainActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.weatherpage).setOnClickListener{
            val intent = Intent(this@MypageActivity, MapActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.cumunitypage).setOnClickListener{
            val intent = Intent(this@MypageActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.mypage).setOnClickListener{
            val intent = Intent(this@MypageActivity, MypageActivity::class.java)
            startActivity(intent)
        }

    }
}