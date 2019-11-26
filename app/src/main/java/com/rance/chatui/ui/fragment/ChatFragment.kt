package com.rance.chatui.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lema.imsdk.bean.chat.LMChatBean
import com.lema.imsdk.bean.chat.LMFriendBean
import com.lema.imsdk.callback.LMBasicBeanCallback
import com.lema.imsdk.callback.LMBasicListCallback
import com.lema.imsdk.client.LMClient
import com.lema.imsdk.util.LMLogUtils
import com.rance.chatui.R
import com.rance.chatui.adapter.ChatDetailAdapter
import com.rance.chatui.adapter.ChatDetailAdapter.OnchatIdClick
import com.rance.chatui.adapter.FriendListAdapter
import com.rance.chatui.adapter.FriendMessageListApapter
import com.rance.chatui.ui.activity.IMActivity
import org.greenrobot.eventbus.EventBus


/**
 * 创建会话
 */
class ChatFragment : Fragment() {

    val recyclerView by lazy { view!!.findViewById<RecyclerView>(R.id.rv_friends) }
    val fmAdapter by lazy { FriendMessageListApapter(activity!!) }
    val tvBack by lazy { view!!.findViewById<TextView>(R.id.tv_back) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // EventBus.getDefault().register(this)
        recyclerView.layoutManager = GridLayoutManager(context, 1) as RecyclerView.LayoutManager? //LinearLayoutManager(activity)
        recyclerView.adapter = fmAdapter
        tvBack.setOnClickListener {
            initData()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    fun initData() {
        LMClient.getChatList(callBackList)
    }

    val callBackList = object : LMBasicListCallback<LMChatBean>() {
        override fun gotResultFail(p0: Int, p1: String?) {
            Toast.makeText(context, "获取好友会话列表失败: $p1", Toast.LENGTH_SHORT).show()
            LMLogUtils.d("daxiong", "====获取好友会话列表失败====" + p1)
        }

        override fun gotResultSuccess(p0: MutableList<LMChatBean>?) {
            Toast.makeText(context, "获取好友会话列表成功", Toast.LENGTH_SHORT).show()
            LMLogUtils.d("daxiong", "====获取好友会话列表成功====")
            fmAdapter.LMChatBeanList = p0
            fmAdapter.setOnFriendClickListener(object : FriendMessageListApapter.OnFriendClickListener {

                override fun onFriendMessageClick(lmFriendBean: LMChatBean) {
                    LMLogUtils.d("daxiong", "====点击了好友====" + lmFriendBean.friend_username)
                    LMLogUtils.d("daxiong", "====点击了好友====" + lmFriendBean.chat_id)

                    if (!TextUtils.isEmpty(lmFriendBean.friend_username) && !TextUtils.isEmpty(lmFriendBean.chat_id)) {
                        val intent = Intent(activity, IMActivity::class.java)
                        intent.putExtra("lm_friend_user_name", lmFriendBean.friend_username)
                        intent.putExtra("lm_friend_chat_id", lmFriendBean.chat_id)
                        startActivity(intent)
                    }
                }

            })
            fmAdapter.notifyDataSetChanged()

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    companion object {
        const val USER_BEAN = "user_bean"

        fun newInstance(): ChatFragment {
            return ChatFragment()
        }
    }
}