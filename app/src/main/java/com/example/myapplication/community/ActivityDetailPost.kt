package com.example.myapplication.community

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.MainActivity
import com.example.myapplication.MypageActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDetailPostBinding
import com.example.myapplication.kdy.LoginActivity
import com.example.myapplication.weather_imgfind.weather.MapActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class ActivityDetailPost : AppCompatActivity() {
    private lateinit var mBinding: ActivityDetailPostBinding
    private lateinit var postInfo: PostDataModel
    private lateinit var replyList: MutableList<String>
    private lateinit var mAdapter: AdapterReplay
    private lateinit var dlg: Dialog
    private var auth : FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("testtesttest", intent.getSerializableExtra("PostInfo").toString())
        postInfo = (intent.getSerializableExtra("PostInfo") as PostDataModel)
        Log.d("testtesttest", "-----${postInfo.replies}-------")
        //replyList = postInfo.replies
        super.onCreate(savedInstanceState)
        mBinding = ActivityDetailPostBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        //checkUserType()
        initVariable()
        //getPostItem()
        //onViewClick()
        setReplyData(postInfo.replies)

        findViewById<TextView>(R.id.loginbuttonmain).setOnClickListener {
            val intent = Intent(this@ActivityDetailPost, LoginActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.logomain).setOnClickListener{
            val intent = Intent(this@ActivityDetailPost, MainActivity::class.java)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.backbtn).setOnClickListener { finish()}
        findViewById<TextView>(R.id.activitytitle).text = "댓글"
        val sharedPref = getSharedPreferences("logininfo", Context.MODE_PRIVATE)
        val nick = sharedPref.getString("nickname", "")
        val url = sharedPref.getString("profileuri", "")
        val logincheck = sharedPref.getBoolean("signedup", false)
        if(logincheck) {
            findViewById<TextView>(R.id.toolbarnick).text = nick
            if(url != "" && url != "null") {
                Glide.with(this)
                    .load(url)
                    .into(findViewById(R.id.toolbarprofile))
            }
            findViewById<TextView>(R.id.loginbuttonmain).visibility = View.GONE
            findViewById<TextView>(R.id.toolbarnick).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.toolbarprofile).visibility = View.VISIBLE
        }

        findViewById<ImageView>(R.id.homepage).setOnClickListener{
            val intent = Intent(this@ActivityDetailPost, MainActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.weatherpage).setOnClickListener{
            val intent = Intent(this@ActivityDetailPost, MapActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.cumunitypage).setOnClickListener{
            val intent = Intent(this@ActivityDetailPost, HomeActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.mypage).setOnClickListener{
            val intent = Intent(this@ActivityDetailPost, MypageActivity::class.java)
            startActivity(intent)
        }

        //댓글 작성 후 보내기 버튼 클릭시 발생 이벤트
        mBinding.imSendDetail.setOnClickListener {
            val updb = FirebaseFirestore.getInstance().collection("BoardPosts").document(postInfo.id)
                .get().addOnSuccessListener {
                    postInfo.replies = (it.data?.get("Posts") as HashMap<String, Any>).get("replies") as ArrayList<Replies>
                    Log.d("##INFO", "***${postInfo.replies}***")
                    val reply: String = mBinding.edReplyDetail.text.toString()
                    Log.i("##TEST", "-----$reply")
                    val pref = getSharedPreferences("logininfo", Context.MODE_PRIVATE)
                    val nowid = pref.getString("id", "").toString()
                    val addReply = Replies(nowid, reply)
                    Log.d("##INFO", "$addReply")
                    postInfo.replies.add(addReply)
                    Log.d("##INFO", "$postInfo")
                    val res = PresenterPost.instance?.setReply(postInfo)
                    if(res!!) {
                        val db = FirebaseFirestore.getInstance().collection("BoardPosts").document(postInfo.id).get()
                            .addOnSuccessListener {
                                val nowdata = it.data?.get("Posts") as HashMap<String, Any>
                                Log.d("##INFO", "$nowdata")
                                mAdapter = AdapterReplay(nowdata.get("replies") as ArrayList<Replies>, nowdata.get("id") as String)
                                mBinding.reRepliesDetail.adapter = mAdapter
                                val linearLayoutManager = LinearLayoutManager(this)
                                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                                mBinding.reRepliesDetail.layoutManager = linearLayoutManager
                                mBinding.edReplyDetail.setText("")
                            }
                    }
                    //댓글 입력시 자동으로 키보드 내림
                    val view = this.currentFocus
                    if (view != null) {
                        val imm =
                            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                }
        }
    }
    private fun initVariable() {
        replyList = ArrayList<String>()
        //mAdapter = AdapterReplay(replyList)
    }

//    private fun getPostItem() {
//        // 넘어온 데이터가 있을 경우
//        if (intent.getSerializableExtra("PostInfo") != null) {
//            postInfo = intent.getSerializableExtra("PostInfo") as PostDataModel
//            Log.i("##INFO", "(): postInfo.replies = ${postInfo.replies.size}");
//            replyList = postInfo.replies
//            mBinding.tvTitleDetailPost.text = postInfo.title
//            mBinding.tvContentDetailPost.text = postInfo.content
//            mBinding.tvRepliesCountDetailPost.text = postInfo.replies.size.toString()
//
//            if (postInfo.pictures.size == 1) {
//                Glide.with(this).load(postInfo.pictures.get(0)).into(mBinding.imOneDetailPost)
//            }
//
//            if (postInfo.pictures.size == 2) {
//                Glide.with(this).load(postInfo.pictures.get(0)).into(mBinding.imOneDetailPost)
//                Glide.with(this).load(postInfo.pictures.get(1)).into(mBinding.imTwoDetailPost)
//                mBinding.imTwoDetailPost.setVisibility(View.VISIBLE)
//            }
//            mBinding.imOneDetailPost.setVisibility(View.VISIBLE)
//        }
//
//        setReplyData()
//    }

    private fun setReplyData(reps : MutableList<Replies>) {
        Log.d("testtesttest", "${reps}")
        mAdapter = AdapterReplay(reps, postInfo.id)
        mBinding.reRepliesDetail.adapter = mAdapter
        mBinding.reRepliesDetail.layoutManager = LinearLayoutManager(this)
        //mAdapter.updateReplyList(replyList)
    }
/*
    private fun onViewClick() {
        mAdapter.onItemClickListener(object : AdapterReplay.OnItemClick {
            override fun clickDelete(reply: String?, position: Int) {
                managePasswordDialog()

                //상단에 취소키를 눌렀을때 다이얼로그창 종료
                dlg.findViewById<View>(R.id.im_cancel_dialog)
                    .setOnClickListener { v: View? -> dlg.dismiss() }

                //댓글 삭제버튼 클릭시
                dlg.findViewById<View>(R.id.bt_ok_dialog).setOnClickListener { v: View? ->
                    val password: String
                    val a: Any = postInfo.replies.get(position)
                    if (a is HashMap<*, *>) {
                        val h =
                            a as HashMap<String, String>
                        h["replayPassword"]
                        password = h["replayPassword"].toString()
                    } else {
                        val h: Replies = a as Replies
                        password = java.lang.String.valueOf(h.replayPassword)
                    }
                    val inputPassword =
                        (dlg.findViewById<View>(R.id.ed_password_dialog) as EditText).text
                            .toString()
                    if (inputPassword == password) {
                        replyList.removeAt(position)
                        //mAdapter.resetReplyList(replyList)
                        postInfo.replies = replyList
                        PresenterPost.instance?.deleteReply(postInfo)
                        dlg.dismiss()
                        mBinding.tvRepliesCountDetailPost.text = replyList!!.size.toString()
                        Toast.makeText(this@ActivityDetailPost, "댓글이 삭제되었습니다", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this@ActivityDetailPost, "비밀번호가 틀립니다", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })
*/
//        mBinding.imBackDetailPost.setOnClickListener { v ->
//            startActivity(Intent(this, HomeActivity::class.java))
//            finish()
//        }
//
//        //댓글 작성 후 보내기 버튼 클릭시 발생 이벤트
//        mBinding.imSendDetail.setOnClickListener { v ->
//            Log.i("##INFO", "reply")
//            managePasswordDialog()
//            dlg.findViewById<View>(R.id.bt_ok_dialog)
//                .setOnClickListener { t: View? ->
//                    val reply: String = mBinding.edReplyDetail.getText().toString()
//                    val inputPassword =
//                        (dlg.findViewById<View>(R.id.ed_password_dialog) as EditText).text
//                            .toString()
//                    val re = Replies(reply, inputPassword.toInt())
//                    Log.i("##INFO", "onViewClick(): re.getReply() = $reply")
//                    replyList.add(re)
//                    Log.i(
//                        "##INFO",
//                        "onViewClick(): replayList.size = " + replyList.size
//                    )
//                    postInfo.replies = replyList
//                    PresenterPost.instance?.setReply(postInfo)
//                    dlg.dismiss()
//                    mAdapter.updateReplyList(replyList)
//                    mBinding.edReplyDetail.setText("")
//                }
//            Log.i("##INFO", "onViewClick(): replayList.size = " + replyList.size)
//            //            mBinding.tvRepliesCountDetailPost.setText(replyList.size() + "");
//
//            //댓글 입력시 자동으로 키보드 내림
//            val view = this.currentFocus
//            if (view != null) {
//                val imm =
//                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(view.windowToken, 0)
//            }
//        }


    }

    //    private fun checkMyFavorite(user : String, post : PostDataModel){
//        Log.d("test1234", "forCheckFavorite")
//        Log.d("test1234", "$user=========$post")
//        if(post.favorites.containsKey(user)) {
//            mBinding.likefill.visibility = View.VISIBLE
//            mBinding.likebtn.visibility = View.GONE
//        } else {
//            mBinding.likebtn.visibility = View.VISIBLE
//            mBinding.likefill.visibility = View.GONE
//        }
//    }
//
/*
    private fun managePasswordDialog() {
        dlg = Dialog(this@ActivityDetailPost, R.style.theme_dialog)
        dlg.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setCanceledOnTouchOutside(false)
        dlg.setCancelable(false)
        dlg.setContentView(R.layout.dialog_check_password)
        dlg.show()
        dlg.findViewById<View>(R.id.im_cancel_dialog)
            .setOnClickListener { t: View? -> dlg.dismiss() }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
        super.onBackPressed()
    }

    companion object {
        private const val TAG = "##H"
    }

}*/
