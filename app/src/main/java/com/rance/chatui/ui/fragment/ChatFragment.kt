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
import com.lema.imsdk.bean.chat.LMChatBean
import com.lema.imsdk.bean.chat.LMChatExecuteBean
import com.lema.imsdk.callback.LMBasicBeanCallback
import com.lema.imsdk.callback.LMBasicListCallback
import com.lema.imsdk.callback.LMChatEventListener
import com.lema.imsdk.client.LMClient
import com.lema.imsdk.util.LMLogUtils
import com.rance.chatui.R
import com.rance.chatui.adapter.FriendMessageListApapter
import com.rance.chatui.record.CommonDialog
import com.rance.chatui.ui.activity.IMActivity


/**
 * 创建会话
 */
class ChatFragment : Fragment(),SwipeRefreshLayout.OnRefreshListener {

    val recyclerView by lazy { view!!.findViewById<RecyclerView>(R.id.rv_friends) }
    val fmAdapter by lazy { FriendMessageListApapter(activity!!) }
    val sfSwiperefreshlayout by lazy { view!!.findViewById<SwipeRefreshLayout>(R.id.srl_swipe_refresh_layout) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // EventBus.getDefault().register(this)
        recyclerView.layoutManager = GridLayoutManager(context, 1) as RecyclerView.LayoutManager? //LinearLayoutManager(activity)
        recyclerView.adapter = fmAdapter
        initData()
        /**
         * 注册会话列表通知回调
         * @param listener
         */
        LMClient.addChatListEventListener(listener)
        sfSwiperefreshlayout.setOnRefreshListener(this) //刷新控件
        sfSwiperefreshlayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun initData() {
        LMClient.getChatList(callBackList)
    }

    override fun onRefresh() {
        initData()
        sfSwiperefreshlayout.isRefreshing = false
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

            p0!!.forEachIndexed { index, lmChatBean ->
                LMLogUtils.d("daxiong", "====好友friend_username====" + lmChatBean.friend_username)
                LMLogUtils.d("daxiong", "====好友friend_chat_id====" + lmChatBean.chat_id)
                LMLogUtils.d("daxiong", "====好友unread_count====" + lmChatBean.unread_count)
            }

            fmAdapter.setOnFriendClickListener(object : FriendMessageListApapter.OnFriendClickListener {

                override fun onFriendMessageClick(lmFriendBean: LMChatBean) {
                    if (!TextUtils.isEmpty(lmFriendBean.friend.username)) {
                        val intent = Intent(activity, IMActivity::class.java)
                        intent.putExtra("lm_friend_user_name", lmFriendBean.friend.username)
                        intent.putExtra("lm_friend_chat_id", lmFriendBean.chat_id)
                        startActivity(intent)
                    }
                }

                override fun onDeleteMessageClick(lmFriendBean: LMChatBean) {

//                    LMLogUtils.d("daxiong", "====好友friend_username====" + lmFriendBean.friend_username)
//                    LMLogUtils.d("daxiong", "====好友friend_chat_id====" + lmFriendBean.chat_id)
//                    LMLogUtils.d("daxiong", "====好友unread_count====" + lmFriendBean.unread_count)
//

                    CommonDialog(context!!, "删除会话", "确认要删除好友${lmFriendBean.friend_username}的会话么？") {
                        if (it) {
                            /**
                             * 删除会话
                             * @param chat_id 会话id
                             * @param callback 结果回调
                             */
                            LMClient.deleteChat(lmFriendBean.chat_id,  lmExecuteBeancallback)
                        }
                    }.show()

                }

            })
            //刷新数据
            fmAdapter.notifyDataSetChanged()

        }
    }


   val lmExecuteBeancallback= object :LMBasicBeanCallback<LMChatExecuteBean>(){
       override fun gotResultFail(p0: Int, p1: String?) {
           Toast.makeText(context, "删除好友失败: $p1", Toast.LENGTH_SHORT).show()
           LMLogUtils.d("daxiong", "====删除好友会话失败====" + p1)
       }

       override fun gotResultSuccess(p0: LMChatExecuteBean?) {
           Toast.makeText(context, "删除好友会话成功", Toast.LENGTH_SHORT).show()
       }
   }



    /**
     * 注册会话列表通知回调
     * @param listener
     */
    val listener = object : LMChatEventListener {
        override fun onChatListChanged() {
            fmAdapter.LMChatBeanList = LMClient.getChatList()
            fmAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //  EventBus.getDefault().unregister(this)
    }


    companion object {
        const val USER_BEAN = "user_bean"

        fun newInstance(): ChatFragment {
            return ChatFragment()
        }
    }
}