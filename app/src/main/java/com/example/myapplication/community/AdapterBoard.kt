package com.example.myapplication.community

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

/**
 * 게시판의 게시글을 보여주는 어댑터 및 게시글의 관리 클래스
 */
class AdapterBoard(list: ArrayList<PostDataModel>) :
    RecyclerView.Adapter<AdapterBoard.ViewHolderMainBoard>() {

    private var pList: ArrayList<PostDataModel>
    private val db = FirebaseFirestore.getInstance()
    private val COLLECTION_PATH = "BoardPosts"
    private lateinit var dlg : Dialog
    private lateinit var nowContext : Context

    init {
        pList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMainBoard {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_main_board_post, parent, false)
        nowContext = view.context
        return ViewHolderMainBoard(view)
    }

    override fun onBindViewHolder(holder: ViewHolderMainBoard, position: Int) {
        Log.d("##INFO", "$pList")
        val postInfo = pList[position]
        holder.fishspecies.text = postInfo.fishspecies
        //holder.replayCount.text = "[${pList[position].replies.size}]"
        if(postInfo.pictures.size > 0) {
            holder.picture.visibility = View.VISIBLE
            Glide.with(holder.itemView)
                .load(postInfo.pictures[0])
                .into(holder.picture)
        }

        holder.content.text = postInfo.content
        //삭제버튼 클릭시 게시글 삭제
        holder.nickname.text = postInfo.nickname
        holder.replyCnt.text = postInfo.replies.size.toString()
        holder.deleteButton.setOnClickListener { v ->
            managePasswordDialog()
            //상단에 취소키를 눌렀을때 다이얼로그창 종료
            dlg.findViewById<View>(R.id.im_cancel_dialog)
                .setOnClickListener { t: View? -> dlg.dismiss() }
            dlg.findViewById<View>(R.id.bt_ok_dialog)
                .setOnClickListener { t: View? ->
                    val password: String = postInfo.password
                    val inputPassword =
                        (dlg.findViewById<View>(R.id.ed_password_dialog) as EditText).text
                            .toString()
                    if (inputPassword == password) {
                        deletePost(postInfo)
                        dlg.dismiss()
                        Toast.makeText(nowContext, "게시글이 삭제되었습니다", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(nowContext, HomeActivity::class.java)
                        nowContext.startActivity(intent)
                        val contextActivity = nowContext as AppCompatActivity
                        contextActivity.finish()
                    } else {
                        Toast.makeText(nowContext, "비밀번호가 틀립니다", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }

        //수정하기 버튼 클릭시 실행 로직
        holder.modifyButton.setOnClickListener { v ->
            managePasswordDialog()
            //상단에 취소키를 눌렀을때 다이얼로그창 종료
            dlg!!.findViewById<View>(R.id.im_cancel_dialog)
                .setOnClickListener { t: View? -> dlg!!.dismiss() }
            dlg!!.findViewById<View>(R.id.bt_ok_dialog)
                .setOnClickListener { t: View? ->
                    val password: String = postInfo.password
                    val inputPassword =
                        (dlg!!.findViewById<View>(R.id.ed_password_dialog) as EditText).text
                            .toString()
                    if (inputPassword == password) {
                        dlg!!.dismiss()
                        val i =
                            Intent(nowContext, ActivityWritePost::class.java)
                        i.putExtra("postInfo", postInfo)
                        nowContext.startActivity(i)
                        val contextActivity = nowContext as AppCompatActivity
                        contextActivity.finish()
                    } else {
                        Toast.makeText(nowContext, "비밀번호가 틀립니다", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }

        val auth = FirebaseAuth.getInstance()
        val nowUser = auth?.currentUser?.uid

        checkMyFavorite(nowUser.toString(), pList[position], holder)

        holder.likeEmptyButton.setOnClickListener{
            Log.d("test1234", "checkempty")
            val firestore = FirebaseFirestore.getInstance()
            val nowDoc = firestore?.collection("BoardPosts")?.document(postInfo.id)
            nowDoc?.get()?.addOnSuccessListener {document ->
                var postUpdate = document?.toObject(PostDataModel::class.java)
                postUpdate?.nickname = postInfo.nickname
                postUpdate?.fishspecies = postInfo.fishspecies
                postUpdate?.content = postInfo.content
                postUpdate?.favoriteCount = postInfo.favoriteCount + 1
                postInfo.favoriteCount += 1
                var nowFavorite = postInfo.favorites
                nowFavorite.put(nowUser.toString(), true)
                postUpdate?.favorites = nowFavorite
                postUpdate?.password = postInfo.password
                postUpdate?.pictures = postInfo.pictures
                postUpdate?.replies = postInfo.replies
                nowDoc.update("Posts", postUpdate)
            }
            holder.likeEmptyButton.visibility = View.GONE
            holder.likeFillButton.visibility = View.VISIBLE
//            holder.likeCnt.text = postInfo.favoriteCount.toString()
//            holder.likeCnt2.text = ""
            holder.likeCnt.text = ""
            holder.likeCnt2.text = postInfo.favoriteCount.toString()
            val intent = Intent(nowContext, HomeActivity::class.java)
            nowContext.startActivity(intent)
            val contextActivity = nowContext as AppCompatActivity
            contextActivity.finish()
        }

        holder.likeFillButton.setOnClickListener{
            Log.d("test1234", "checkfill")
            val firestore = FirebaseFirestore.getInstance()
            val nowDoc = firestore?.collection("BoardPosts")?.document(postInfo.id)
            nowDoc?.get()?.addOnSuccessListener {document ->
                var postUpdate = document?.toObject(PostDataModel::class.java)
                postUpdate?.nickname = postInfo.nickname
                postUpdate?.fishspecies = postInfo.fishspecies
                postUpdate?.content = postInfo.content
                postUpdate?.favoriteCount = postInfo.favoriteCount - 1
                postInfo.favoriteCount -= 1
                var nowFavorite = postInfo.favorites
                nowFavorite.remove(nowUser.toString())
                postUpdate?.favorites = nowFavorite
                postUpdate?.password = postInfo.password
                postUpdate?.pictures = postInfo.pictures
                postUpdate?.replies = postInfo.replies
                nowDoc.update("Posts", postUpdate)
            }
            holder.likeEmptyButton.visibility = View.VISIBLE
            holder.likeFillButton.visibility = View.GONE
//            holder.likeCnt.text = ""
//            holder.likeCnt2.text = postInfo.favoriteCount.toString()
            holder.likeCnt.text = postInfo.favoriteCount.toString()
            holder.likeCnt2.text = ""
            val intent = Intent(nowContext, HomeActivity::class.java)
            //intent.addFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
            nowContext.startActivity(intent)
            (nowContext as? Activity)?.overridePendingTransition(0, 0)
            val contextActivity = nowContext as AppCompatActivity
            contextActivity.finish()
            //(nowContext as Activity).overridePendingTransition(0, 0)
            //val contextActivity = nowContext as AppCompatActivity
            //contextActivity.finish()
            //(nowContext as Activity).overridePendingTransition(0, 0)
        }
        holder.onItemClick()
    }

    private fun checkMyFavorite(user : String, post : PostDataModel, holder: ViewHolderMainBoard){
        Log.d("test1234", "forCheckFavorite")
        Log.d("test1234", "$user=========$post")
        if(post.favorites.containsKey(user)) {
            holder.likeFillButton.visibility = View.VISIBLE
            holder.likeEmptyButton.visibility = View.GONE
            holder.likeCnt.text = ""
            holder.likeCnt2.text = post.favoriteCount.toString()
        } else {
            holder.likeFillButton.visibility = View.GONE
            holder.likeEmptyButton.visibility = View.VISIBLE
            holder.likeCnt.text = post.favoriteCount.toString()
            holder.likeCnt2.text = ""
        }

    }

    private fun managePasswordDialog() {
        dlg = Dialog(nowContext, R.style.theme_dialog)
        dlg.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setCanceledOnTouchOutside(false)
        dlg.setCancelable(false)
        dlg.setContentView(R.layout.dialog_check_password)
        dlg.show()
        dlg.findViewById<View>(R.id.im_cancel_dialog)
            .setOnClickListener { t: View? -> dlg.dismiss() }
    }

    fun deleteReply(postInfo: PostDataModel): Boolean {
        postInfo.id?.let {
            db.collection(COLLECTION_PATH).document(it).update("Posts", postInfo)
                .addOnSuccessListener {
                    Log.i(
                        "##INFO",
                        "deleteReply(): success"
                    )
                }.addOnFailureListener { e ->
                    Log.i(
                        "##INFO",
                        "deleteReply(): e = " + e.message
                    )
                }
        }
        return true
    }

    //게시글을 삭제하는 메서드
    fun deletePost(postInfo: PostDataModel): Boolean {
        val updates = hashMapOf<String, Any>(
            "Posts" to FieldValue.delete(),
        )

        postInfo.id.let {
            db.collection(COLLECTION_PATH).document(it).delete().addOnSuccessListener {
                Log.i(
                    "##INFO",
                    "deletePost(): success"
                )
            }.addOnFailureListener { e ->
                Log.i(
                    "##INFO",
                    "deletePost(): e = " + e.message
                )
            }
        }
        return true
    }

    override fun getItemCount(): Int {
        return pList.size
    }

    fun updatePostList(list: ArrayList<PostDataModel>) {
        pList = list
        notifyDataSetChanged()
    }

    fun resetPostList(list: ArrayList<PostDataModel>) {
        pList = list
        notifyDataSetChanged()
    }

    inner class ViewHolderMainBoard(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val title: TextView
        val content : TextView
        //val replayCount: TextView
        val picture: ImageView
        val likeEmptyButton : Button
        val likeFillButton : Button
        val modifyButton : TextView
        val deleteButton : TextView
        val nickname : TextView
        val likeCnt : TextView
        val likeCnt2 : TextView
        val replyCnt : TextView
        val fishspecies : TextView

        init {
            //title = itemView.findViewById(R.id.tv_title_detail_post)
            content = itemView.findViewById(R.id.tv_content_detail_post)
            //replayCount = itemView.findViewById<TextView>(R.id.tv_re_count_item_post)
            picture = itemView.findViewById(R.id.im_one_detail_post)
            likeEmptyButton = itemView.findViewById(R.id.likebtn)
            likeFillButton = itemView.findViewById(R.id.likefill)
            modifyButton = itemView.findViewById(R.id.tv_modify_content)
            deleteButton = itemView.findViewById(R.id.tv_delete_content)
            nickname = itemView.findViewById(R.id.boardNickname)
            likeCnt = itemView.findViewById(R.id.emptyCnt)
            likeCnt2 = itemView.findViewById(R.id.fullCnt)
            replyCnt = itemView.findViewById(R.id.tv_replies_count_detail_post)
            fishspecies = itemView.findViewById(R.id.tv_fishspecies)
            onItemClick()
        }

        fun onItemClick() {
            itemView.setOnClickListener { v: View? ->
                val i = Intent(
                    itemView.context,
                    ActivityDetailPost::class.java
                )

                i.putExtra("PostInfo", pList[adapterPosition])
                itemView.context.startActivity(i)
            }
        }
    }
}
