package com.rance.chatui.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lema.imsdk.bean.chat.LMFriendBean
import com.lema.imsdk.callback.LMBasicApiCallback
import com.lema.imsdk.callback.LMBasicListCallback
import com.lema.imsdk.client.LMClient
import com.lema.imsdk.util.LMLogUtils
import com.rance.chatui.R
import com.rance.chatui.adapter.FriendManagementAdapter

/**
 * author: daxiong
 * created on: 2019-11-23 10:53
 * -----------------------------------------------
 * description:
 * -----------------------------------------------
 */
class FriendManagementFragment : Fragment() {

    val recyclerView by lazy { view!!.findViewById<RecyclerView>(R.id.rv_friends) }
    val fmAdapter by lazy { FriendManagementAdapter() }
    val tvBack by lazy { view!!.findViewById<TextView>(R.id.tv_back) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //   EventBus.getDefault().register(this)
        recyclerView.layoutManager = GridLayoutManager(context, 1) as RecyclerView.LayoutManager? //LinearLayoutManager(activity)
        recyclerView.adapter = fmAdapter
        tvBack.setOnClickListener {
            initData()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friend_management, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  初始化数据
        initData()

    }

    fun initData() {
        LMClient.friendRequsetList(callBackList)
    }

    val callBackList = object : LMBasicListCallback<LMFriendBean>() {
        override fun gotResultFail(p0: Int, p1: String?) {
            Toast.makeText(context, "获取好友申请列表失败: $p1", Toast.LENGTH_SHORT).show()
            LMLogUtils.d("daxiong", "====获取好友申请列表失败====" + p1)
        }

        override fun gotResultSuccess(p0: MutableList<LMFriendBean>?) {
            Toast.makeText(context, "获取好友申请列表成功", Toast.LENGTH_SHORT).show()
            LMLogUtils.d("daxiong", "====获取好友申请列表成功====")
            fmAdapter.LMFriendBeanList = p0
            fmAdapter.setOnFriendClickListener(object : FriendManagementAdapter.OnFriendClickListener {

                override fun onAgreeFriendClick(position: Int) {
                    p0?.let {
                        LMClient.friendApprove(p0[position].username, 1, addCallback)
                    }
                }

                override fun onRefuseFriendClick(position: Int) {
                    p0?.let {
                        LMClient.friendApprove(p0[position].username, -1, addCallback)
                    }
                }
            })
            fmAdapter.notifyDataSetChanged()
        }
    }

    val addCallback = object : LMBasicApiCallback() {
        override fun gotResultFail(p0: Int, p1: String?) {
            Toast.makeText(context, "操作失败: $p1", Toast.LENGTH_SHORT).show()
        }

        override fun gotResultSuccess() {
            Toast.makeText(context, "操作成功:", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        //EventBus.getDefault().unregister(this)
    }


    companion object {
        fun newInstance(): FriendManagementFragment {
            return FriendManagementFragment()
        }
    }
}