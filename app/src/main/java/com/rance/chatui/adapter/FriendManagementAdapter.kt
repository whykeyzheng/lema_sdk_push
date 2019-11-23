package com.rance.chatui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.lema.imsdk.bean.chat.LMFriendBean
import com.rance.chatui.R

/**
 * author: daxiong
 * created on: 2019-11-23 11:40
 * -----------------------------------------------
 * description:
 * -----------------------------------------------
 */
class FriendManagementAdapter(var LMFriendBeanList: MutableList<LMFriendBean>? = null) : RecyclerView.Adapter<FriendManagementAdapter.ViewHolder>(), View.OnClickListener {
    private var mOnFriendClickListener: FriendManagementAdapter.OnFriendClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendManagementAdapter.ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
//        val viewHolder = FriendManagementAdapter.ViewHolder(view)
//        viewHolder.itemView.setOnClickListener(this)
//        return viewHolder
        return ViewHolder(View.inflate(parent.context, R.layout.item_friends_management, null))
    }

    override fun onBindViewHolder(holder: FriendManagementAdapter.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener(this)
        LMFriendBeanList?.let {
            val LMFriendBean = it[position]
            holder.tvName.text = LMFriendBean.username
            holder.itemView.tag = LMFriendBean
        }

    }


    override fun getItemCount(): Int {
        return LMFriendBeanList?.size ?: 0
    }

    override fun onClick(v: View) {
        if (mOnFriendClickListener == null) {
            return
        }

        mOnFriendClickListener!!.onFriendClick(v, v.tag as LMFriendBean)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var tvName: TextView

        init {
            tvName = itemView.findViewById<View>(R.id.tv_name) as TextView

        }
    }

    interface OnFriendClickListener {
        fun onFriendClick(view: View, LMFriendBean: LMFriendBean)
    }

    fun setOnFriendClickListener(onFriendClickListener: FriendManagementAdapter.OnFriendClickListener) {
        this.mOnFriendClickListener = onFriendClickListener
    }

}

