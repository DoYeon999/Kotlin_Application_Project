package com.example.myapplication.community

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class AdapterReplay(list: ArrayList<Replies>) :
    RecyclerView.Adapter<AdapterReplay.ViewHolderReplay>() {
    private var rList: ArrayList<Replies>
    private var callback: OnItemClick? = null

    init {
        rList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderReplay {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_reply, parent, false)
        return ViewHolderReplay(view)
    }

    override fun onBindViewHolder(holder: ViewHolderReplay, position: Int) {
        val a: Any = rList[position]
        if (a is HashMap<*, *>) {
            val h = a as HashMap<String, String>
            holder.replyContent.text = h["reply"]
            Log.i("##INFO", "onBindViewHolder(): h.get(\"reply\") = " + h["reply"])
        } else {
            val h: Replies = a as Replies
            holder.replyContent.text = h.reply
            Log.i("##INFO", "onBindViewHolder(): h = " + h.reply)
            Log.i(
                "##INFO",
                "onBindViewHolder(): rList.get(position) = " + rList[position].reply
            )
        }
    }

    override fun getItemCount(): Int {
        return rList.size
    }

    fun updateReplyList(list: ArrayList<Replies>) {
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
}