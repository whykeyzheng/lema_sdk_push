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
import com.rance.chatui.adapter.FriendListAdapter

/**
 * author: daxiong
 * created on: 2019-11-25 11:48
 * -----------------------------------------------
 * description:
 * -----------------------------------------------
 */
class FriendListFrament : Fragment(){

    val recyclerView by lazy { view!!.findViewById<RecyclerView>(R.id.rv_friends) }
    val fmAdapter by lazy { FriendListAdapter() }
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
        return inflater.inflate(R.layout.fragment_friend_list, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()


    }

    fun initData() {
        LMClient.friendList(callBackList)
    }

    val callBackList = object : LMBasicListCallback<LMFriendBean>() {
        override fun gotResultFail(p0: Int, p1: String?) {
            Toast.makeText(context, "获取好友列表失败: $p1", Toast.LENGTH_SHORT).show()
            LMLogUtils.d("daxiong", "====获取好友列表失败====" + p1)
        }

        override fun gotResultSuccess(p0: MutableList<LMFriendBean>?) {
            Toast.makeText(context, "获取好友列表成功", Toast.LENGTH_SHORT).show()
            LMLogUtils.d("daxiong", "====获取好友列表成功====")

            p0!!.forEachIndexed { index, lmFriendBean ->
                LMLogUtils.d("daxiong", "====打印第 $index 个好友开始====")
                LMLogUtils.d("daxiong", "====my_username====" + lmFriendBean.my_username)
                LMLogUtils.d("daxiong", "====username====" + lmFriendBean.username)
                LMLogUtils.d("daxiong", "====chat_id====" + lmFriendBean.chat_id)
                LMLogUtils.d("daxiong", "====status====" + lmFriendBean.status)
                LMLogUtils.d("daxiong", "====打印第 $index 个好友结束====")
            }

            fmAdapter.LMFriendBeanList = p0
            fmAdapter.setOnFriendClickListener(object : FriendListAdapter.OnFriendClickListener{

                override fun onAgreeFriendClick(position: Int) {
                    LMLogUtils.d("daxiong", "====点击==tv_agree=="+p0[position].username)

                    LMClient.friendApprove(p0[position].username,1, addCallback)
                }

                override fun onRefuseFriendClick(position: Int) {
                    LMLogUtils.d("daxiong", "====点击==tv_region=="+p0[position].username)
                    LMClient.friendApprove(p0[position].username,-1, addCallback)
                }
            })
            fmAdapter.notifyDataSetChanged()

        }
    }

    val addCallback = object : LMBasicApiCallback(){
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
        fun newInstance(): FriendListFrament {
            return FriendListFrament()
        }
    }
}