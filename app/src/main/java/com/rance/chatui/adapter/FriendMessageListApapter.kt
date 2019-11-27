package com.rance.chatui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lema.imsdk.bean.chat.LMChatBean
import com.lema.imsdk.bean.chat.LMFriendBean
import com.rance.chatui.R

/**
 * author: daxiong
 * created on: 2019-11-26 15:08
 * -----------------------------------------------
 * description:
 * -----------------------------------------------
 */
class FriendMessageListApapter(var context: Context, var LMChatBeanList: MutableList<LMChatBean>? = null) : RecyclerView.Adapter<FriendMessageListApapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendMessageListApapter.ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
//        val viewHolder = FriendManagementAdapter.ViewHolder(view)
//        viewHolder.itemView.setOnClickListener(this)
//        return viewHolder
        return ViewHolder(View.inflate(parent.context, R.layout.item_friend_message, null))
    }

    override fun onBindViewHolder(holder: FriendMessageListApapter.ViewHolder, position: Int) {

        LMChatBeanList?.let {
            val lmChatBean = it[position]
            Glide.with(context).load(lmChatBean.avatar).placeholder(R.mipmap.ic_start_logo).error(R.mipmap.ic_start_logo).into(holder.tvAvatar)
            holder.tvName.text = lmChatBean.friend_username
            holder.tvCount.text ="未读消息" +lmChatBean.unread_count.toString()+"条"
            holder.itemView.setOnClickListener {
                mOnFriendClickListener!!.onFriendMessageClick(lmChatBean)
            }
            holder.itemView.setOnLongClickListener {
                mOnFriendClickListener!!.onDeleteMessageClick(lmChatBean)
                return@setOnLongClickListener true
            }
        }

    }

    override fun getItemCount(): Int {
        return LMChatBeanList?.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById<View>(R.id.tv_name) as TextView
        var tvAvatar: ImageView = itemView.findViewById<View>(R.id.iv_avatar) as ImageView
        var tvCount: TextView = itemView.findViewById<View>(R.id.tv_count) as TextView

    }
    private var mOnFriendClickListener: FriendMessageListApapter.OnFriendClickListener? = null
    interface OnFriendClickListener {
        fun onFriendMessageClick(lmFriendBean: LMChatBean)
        fun onDeleteMessageClick(lmFriendBean: LMChatBean)
    }

    fun setOnFriendClickListener(onFriendClickListener: FriendMessageListApapter.OnFriendClickListener) {
        this.mOnFriendClickListener = onFriendClickListener
    }

}
