package com.rance.chatui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lema.imsdk.bean.chat.LMChatBean
import com.rance.chatui.R

/**
 * 聊天详情列表
 */
class ChatDetailAdapter(private val imContactList: List<LMChatBean>) : RecyclerView.Adapter<ChatDetailAdapter.ViewHolder>(), View.OnClickListener {
    private var mOnChatClick: OnchatIdClick? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_detail, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener(this)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imContact = imContactList[position]
        holder.tvName.text = imContact.my_username
        holder.tvChatId.text = imContact.chat_id
        holder.itemView.tag = imContact
    }

    override fun getItemCount(): Int {
        return imContactList.size
    }

    override fun onClick(v: View) {
        if (mOnChatClick == null) {
            return
        }
        mOnChatClick!!.onIdClick(v, v.tag as LMChatBean)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView
        var tvChatId: TextView

        init {
            tvName = itemView.findViewById<View>(R.id.tv_name) as TextView
            tvChatId = itemView.findViewById<View>(R.id.tv_chat_id) as TextView
        }
    }

    interface OnchatIdClick {
        fun onIdClick(view: View?, imContact: LMChatBean?)
    }

    fun setmOnChatClickListener(onchatIdClick: OnchatIdClick?) {
        mOnChatClick = onchatIdClick
    }

}