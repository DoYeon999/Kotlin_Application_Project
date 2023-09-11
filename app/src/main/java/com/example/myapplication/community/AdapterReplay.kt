package com.example.myapplication.community

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemPracticeBinding
import com.example.myapplication.databinding.ItemReplyBinding
import com.example.myapplication.weather_imgfind.adapter.APIViewHolder
import com.example.myapplication.weather_imgfind.model.TideModel
import com.example.myapplication.weather_imgfind.model.temper
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.contracts.contract


class ReplyHolder(val binding : ItemReplyBinding) : RecyclerView.ViewHolder(binding.root)

// 커스텀 어댑터
// onCreateViewHolder
// getItemCount
// onBindViewHolder를 Implement할 것
class AdapterReplay(val datas : MutableList<Replies>, val postId : String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //private var callback: OnItemClick? = null
    //private val cancel: ImageView? = null
    lateinit var nowContext : Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_detail_post, parent, false)
        nowContext = view.context

        return ReplyHolder(
            ItemReplyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun getItemCount(): Int {
        return datas?.size!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val nowbinding = (holder as ReplyHolder).binding
        val sharedPref = nowContext.getSharedPreferences("logininfo", Context.MODE_PRIVATE)
        val nowid = sharedPref.getString("id", "")
        val nowRp = datas[position] as HashMap<String, String>
        val replyId = nowRp.get("reply_id")
        if(nowid == replyId) {
            nowbinding.btPopupReply.visibility = View.VISIBLE
        }
        val logincheck = sharedPref.getBoolean("signedup", false)
        if(logincheck) {
            nowbinding.btPopupReply.setOnClickListener {
                val popup = PopupMenu(nowContext, holder.binding.btPopupReply) // PopupMenu 객체 선언
                popup.menuInflater.inflate(R.menu.popup, popup.menu) // 메뉴 레이아웃 inflate
                //popup.setOnMenuItemClickListener(this)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.tv_modify_content -> {
                            Log.d("##INFO", "modify reply")
                            val dialogView = LayoutInflater.from(nowContext).inflate(R.layout.reply_modify_dialog, null)
                            val dialog = AlertDialog.Builder(nowContext)
                                .setTitle("댓글 수정")
                                .setView(dialogView)
                                .setPositiveButton("수정") { dialog, now
                                    -> val modifiedReply = dialogView.findViewById<EditText>(R.id.reply_edit).text
                                    nowRp["reply"] = modifiedReply.toString()
                                    notifyItemChanged(position)
                                    val nowDoc = FirebaseFirestore.getInstance().collection("BoardPosts").document(postId)
                                    nowDoc.get().addOnSuccessListener {
                                        val now = ((it.data)?.get("Posts")) as HashMap<String, Any>
                                        now.put("replies", datas)
                                        Log.d("##INFO", "$now")
                                        val updateReply = hashMapOf<String, Any>(
                                            "Posts" to now
                                        )
                                        nowDoc.update(updateReply)
                                            .addOnSuccessListener {
                                                Toast.makeText(nowContext, "댓글이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(nowContext, "댓글 수정 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                }
                                .setNegativeButton("취소") { dialog, now
                                    -> dialog.dismiss()
                                }.create()
                            dialog.show()
                            true
                        }
                        R.id.tv_delete_content -> {
                            Log.d("##INFO", "remove reply")
                            val dialog = AlertDialog.Builder(nowContext)
                                .setTitle("댓글 삭제")
                                .setMessage("댓글을 삭제하시겠습니까?")
                                .setPositiveButton("예") { it, now ->
                                    datas.removeAt(position)
                                    notifyDataSetChanged()
                                    val nowDoc = FirebaseFirestore.getInstance().collection("BoardPosts").document(postId)
                                    nowDoc.get().addOnSuccessListener {
                                        val now = ((it.data)?.get("Posts")) as HashMap<String, Any>
                                        now.put("replies", datas)
                                        Log.d("##INFO", "$now")
                                        val updateReply = hashMapOf<String, Any>(
                                            "Posts" to now
                                        )
                                        nowDoc.update(updateReply)
                                            .addOnSuccessListener {
                                                Toast.makeText(nowContext, "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(nowContext, "댓글 삭제 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                }
                                .setNegativeButton("아니오") { it, now ->
                                    it.dismiss()
                                }.create()
                            dialog.show()
                            true
                        }
                        else -> false
                    }
                }
                popup.show() // 팝업 보여주기
            }
        } else {
            nowbinding.btPopupReply.visibility = View.GONE
        }

        nowbinding.tvReply.text = nowRp.get("reply")
        //onItemClick(position)

    }
}
/*
    private fun onItemClick(position : Int) {
        cancel?.setOnClickListener { v: View? ->
            val a = datas?.get(position)!!
            callback!!.clickDelete(datas?.get(position)?.reply, position)
        }
    }

    fun onItemClickListener(cb: OnItemClick?) {
        callback = cb
    }

    interface OnItemClick {
        fun clickDelete(reply: String?, position: Int)
    }
}
*/
/*
class AdapterReplay(list: MutableList<Replies>) :
    RecyclerView.Adapter<AdapterReplay.ViewHolderReplay>() {
    private var rList: MutableList<Replies>
    private var callback: OnItemClick? = null
    private lateinit var view : View

    init {
        rList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderReplay {
        view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_reply, parent, false)
        return ViewHolderReplay(view)
    }

    override fun onBindViewHolder(holder: ViewHolderReplay, position: Int) {
        Log.d("testtesttest", "gsdgaasdgsadgsadgasdgdsagsadgads")
        val a = rList[position]
        view.findViewById<TextView>(R.id.tv_reply).text = a.reply

//        holder.replyContent.text = a.reply
//        if (a is HashMap<*, *>) {
//            val h = a as HashMap<String, String>
//            holder.replyContent.text = h["reply"]
//            Log.i("##INFO", "onBindViewHolder(): h.get(\"reply\") = " + h["reply"])
//        } else {
//            val h: Replies = a as Replies
//            holder.replyContent.text = h.reply
//            Log.i("##INFO", "onBindViewHolder(): h = " + h.reply)
//            Log.i(
//                "##INFO",
//                "onBindViewHolder(): rList.get(position) = " + rList[position].reply
//            )
//        }
    }

    override fun getItemCount(): Int {
        return rList.size
    }

    fun updateReplyList(list: MutableList<Replies>) {
        rList = list
        notifyDataSetChanged()
    }

    fun resetReplyList(list: ArrayList<Replies>) {
        rList = list
        notifyDataSetChanged()
    }

    inner class ViewHolderReplay(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val replyContent: TextView
        private val cancel: ImageView

        init {
            replyContent = itemView.findViewById<TextView>(R.id.tv_reply)
            cancel = itemView.findViewById<ImageView>(R.id.im_delete_reply)
            onItemClick()
        }

        private fun onItemClick() {
            cancel.setOnClickListener { v: View? ->
                val a: Any = rList[adapterPosition]
                if (a is HashMap<*, *>) {
                    val h =
                        a as HashMap<String, String>
                    callback!!.clickDelete(h["reply"], adapterPosition)
                } else {
                    val h: Replies = a as Replies
                    callback!!.clickDelete(h.reply, adapterPosition)
                }
            }
        }
    }

    fun onItemClickListener(cb: OnItemClick?) {
        callback = cb
    }

    interface OnItemClick {
        fun clickDelete(reply: String?, position: Int)
    }
}*/