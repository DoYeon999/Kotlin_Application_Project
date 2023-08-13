package com.example.myapplication.community

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemPracticeBinding
import com.example.myapplication.databinding.ItemReplyBinding
import com.example.myapplication.weather_imgfind.adapter.APIViewHolder
import com.example.myapplication.weather_imgfind.model.TideModel
import com.example.myapplication.weather_imgfind.model.temper
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ReplyHolder(val binding : ItemReplyBinding) : RecyclerView.ViewHolder(binding.root)

// 커스텀 어댑터
// onCreateViewHolder
// getItemCount
// onBindViewHolder를 Implement할 것
class AdapterReplay(val datas : MutableList<String>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //private var callback: OnItemClick? = null
    //private val cancel: ImageView? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

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
        Log.d("testtesttest", "//${datas?.get(position)}//")
        nowbinding.tvReply.text = datas?.get(position)
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