package com.rance.chatui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lema.imsdk.bean.chat.LMFriendBean
import com.rance.chatui.R

/**
 * author: daxiong
 * created on: 2019-11-25 11:53
 * -----------------------------------------------
 * description:
 * -----------------------------------------------
 */
class FriendListAdapter(var LMFriendBeanList: MutableList<LMFriendBean>? = null) : RecyclerView.Adapter<FriendListAdapter.ViewHolder>() {
    private var mOnFriendClickListener: FriendListAdapter.OnFriendClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendListAdapter.ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
//        val viewHolder = FriendManagementAdapter.ViewHolder(view)
//        viewHolder.itemView.setOnClickListener(this)
//        return viewHolder
        return ViewHolder(View.inflate(parent.context, R.layout.item_friend_list, null))
    }

    override fun onBindViewHolder(holder: FriendListAdapter.ViewHolder, position: Int) {

        LMFriendBeanList?.let {
            val lmFriendBean = it[position]
            holder.tvName.text = lmFriendBean.username
            holder.itemView.setOnClickListener {
                mOnFriendClickListener!!.onFriendMessageClick(lmFriendBean)
            }
        }

    }

    override fun getItemCount(): Int {
        return LMFriendBeanList?.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById<View>(R.id.tv_name) as TextView

    }

    interface OnFriendClickListener {
        fun onFriendMessageClick(lmFriendBean: LMFriendBean)
    }

    fun setOnFriendClickListener(onFriendClickListener: FriendListAdapter.OnFriendClickListener) {
        this.mOnFriendClickListener = onFriendClickListener
    }

}
