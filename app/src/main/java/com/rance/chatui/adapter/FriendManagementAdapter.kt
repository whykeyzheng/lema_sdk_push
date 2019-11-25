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
 * created on: 2019-11-23 11:40
 * -----------------------------------------------
 * description:
 * -----------------------------------------------
 */
class FriendManagementAdapter(var LMFriendBeanList: MutableList<LMFriendBean>? = null) : RecyclerView.Adapter<FriendManagementAdapter.ViewHolder>() {
    private var mOnFriendClickListener: FriendManagementAdapter.OnFriendClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendManagementAdapter.ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
//        val viewHolder = FriendManagementAdapter.ViewHolder(view)
//        viewHolder.itemView.setOnClickListener(this)
//        return viewHolder
        return ViewHolder(View.inflate(parent.context, R.layout.item_friends_management, null))
    }

    override fun onBindViewHolder(holder: FriendManagementAdapter.ViewHolder, position: Int) {

        LMFriendBeanList?.let {
            val lmFriendBean = it[position]
            if (lmFriendBean.status == 1 || lmFriendBean.status == -1) {
                holder.tvAgree.isVisible = false
                holder.tvRefuse.isVisible = false
                holder.tvStatus.isVisible = true
                holder.tvDelete.isVisible = true
                when (lmFriendBean.status) {
                    1 -> {
                        holder.tvStatus.text = "已同意"
                    }
                    -1 -> {
                        holder.tvStatus.text = "已拒绝"
                    }
                }
            } else {
                holder.tvStatus.isVisible = false
                holder.tvAgree.isVisible = true
                holder.tvRefuse.isVisible = true
                holder.tvDelete.isVisible = false

            }
            holder.tvName.text = lmFriendBean.username
            holder.tvAgree.setOnClickListener {
                mOnFriendClickListener!!.onAgreeFriendClick(position)
            }
            holder.tvRefuse.setOnClickListener {
                mOnFriendClickListener!!.onRefuseFriendClick(position)
            }

            holder.tvDelete.setOnClickListener {
                mOnFriendClickListener!!.onDeleteFriendClick(position)
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
        var tvStatus: TextView = itemView.findViewById<View>(R.id.tv_status) as TextView
        var tvDelete: TextView = itemView.findViewById<View>(R.id.tv_delete) as TextView

    }

    interface OnFriendClickListener {
        fun onAgreeFriendClick(position: Int)
        fun onRefuseFriendClick(position: Int)
        fun onDeleteFriendClick(position: Int)
    }

    fun setOnFriendClickListener(onFriendClickListener: FriendManagementAdapter.OnFriendClickListener) {
        this.mOnFriendClickListener = onFriendClickListener
    }

}

