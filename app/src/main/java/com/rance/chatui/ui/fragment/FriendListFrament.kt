package com.rance.chatui.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.lema.imsdk.bean.chat.LMFriendBean
import com.lema.imsdk.callback.LMBasicListCallback
import com.lema.imsdk.client.LMClient
import com.lema.imsdk.util.LMLogUtils
import com.rance.chatui.R
import com.rance.chatui.adapter.FriendListAdapter
import com.rance.chatui.eventbus.FriendLlistEventBus
import com.rance.chatui.ui.activity.IMActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * author: daxiong
 * created on: 2019-11-25 11:48
 * -----------------------------------------------
 * description:
 * -----------------------------------------------
 */
class FriendListFrament : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    val recyclerView by lazy { view!!.findViewById<RecyclerView>(R.id.rv_friends) }
    val fmAdapter by lazy { FriendListAdapter() }
    val sfSwiperefreshlayout by lazy { view!!.findViewById<SwipeRefreshLayout>(R.id.srl_swipe_refresh_layout) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        EventBus.getDefault().register(this)
        recyclerView.layoutManager = GridLayoutManager(context, 1) as RecyclerView.LayoutManager? //LinearLayoutManager(activity)
        recyclerView.adapter = fmAdapter
        sfSwiperefreshlayout.setOnRefreshListener(this) //刷新控件
        sfSwiperefreshlayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light)
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

    override fun onRefresh() {
        initData()
        sfSwiperefreshlayout.isRefreshing = false
    }

    val callBackList = object : LMBasicListCallback<LMFriendBean>() {
        override fun gotResultFail(p0: Int, p1: String?) {
            Toast.makeText(context, "获取好友列表失败: $p1", Toast.LENGTH_SHORT).show()
            LMLogUtils.d("daxiong", "====获取好友列表失败====" + p1)
        }

        override fun gotResultSuccess(p0: MutableList<LMFriendBean>?) {
            Toast.makeText(context, "获取好友列表成功", Toast.LENGTH_SHORT).show()
            LMLogUtils.d("daxiong", "====获取好友列表成功====")

            fmAdapter.LMFriendBeanList = p0
            fmAdapter.setOnFriendClickListener(object : FriendListAdapter.OnFriendClickListener {

                override fun onFriendMessageClick(lmFriendBean: LMFriendBean) {
                    LMLogUtils.d("daxiong", "====点击了好友====" + lmFriendBean.username)

                    if (!TextUtils.isEmpty(lmFriendBean.username) && !TextUtils.isEmpty(lmFriendBean.chat_id)) {
                        val intent = Intent(activity, IMActivity::class.java)
                        intent.putExtra("lm_friend_user_name", lmFriendBean.username)
                        intent.putExtra("lm_friend_chat_id", lmFriendBean.chat_id)
                        startActivity(intent)
                    }
                }

            })
            fmAdapter.notifyDataSetChanged()

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUserInfoEvent(event: FriendLlistEventBus) {//接收
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    companion object {
        fun newInstance(): FriendListFrament {
            return FriendListFrament()
        }
    }
}