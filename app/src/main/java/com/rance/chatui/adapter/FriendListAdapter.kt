package com.rance.chatui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
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
class FriendListAdapter (var LMFriendBeanList: MutableList<LMFriendBean>? = null) : RecyclerView.Adapter<FriendListAdapter.ViewHolder>() {
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
            if (lmFriendBean.status == 1) {
                holder.tvAgree.isVisible = false
                holder.tvRefuse.isVisible = false
            } else {
                holder.tvAgree.isVisible = true
                holder.tvRefuse.isVisible = true
            }
            holder.tvName.text = lmFriendBean.username
            holder.tvAgree.setOnClickListener {
                mOnFriendClickListener!!.onAgreeFriendClick(position)
            }
            holder.tvRefuse.setOnClickListener {
                mOnFriendClickListener!!.onRefuseFriendClick(position)
            }

        }

    }


    override fun getItemCount(): Int {
        return LMFriendBeanList?.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById<View>(R.id.tv_name) as TextView
        var tvAgree: TextView = itemView.findViewById<View>(R.id.tv_agree) as TextView
        var tvRefuse: TextView = itemView.findViewById<View>(R.id.tv_refuse) as TextView

    }

    interface OnFriendClickListener {
        fun onAgreeFriendClick(position: Int)
        fun onRefuseFriendClick(position: Int)
    }

    fun setOnFriendClickListener(onFriendClickListener: FriendListAdapter.OnFriendClickListener) {
        this.mOnFriendClickListener = onFriendClickListener
    }

}
