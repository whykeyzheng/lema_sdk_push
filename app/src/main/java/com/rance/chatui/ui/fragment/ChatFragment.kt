package com.rance.chatui.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.lema.imsdk.bean.chat.LMChatBean
import com.lema.imsdk.callback.LMBasicBeanCallback
import com.lema.imsdk.client.LMClient
import com.rance.chatui.R
import com.rance.chatui.adapter.ChatDetailAdapter
import com.rance.chatui.adapter.ChatDetailAdapter.OnchatIdClick
import com.rance.chatui.ui.activity.IMActivity


/**
 * 创建会话
 */
class ChatFragment : Fragment(), OnchatIdClick {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    var btnDetail: Button? = null
    // var recyclerView: RecyclerView? = null
    private val adapter: ChatDetailAdapter? = null
    private var bean: LMChatBean? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        //初始化
        btnDetail = view.findViewById(R.id.btn_detail)
        //   recyclerView = view.findViewById(R.id.recycler_view)
        setListener()
        return view
    }

    private fun setListener() {
        btnDetail!!.setOnClickListener {
            val intent = Intent(activity, IMActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onIdClick(view: View?, imContact: LMChatBean?) {}

    companion object {
        const val USER_BEAN = "user_bean"

        fun newInstance(): ChatFragment {
            return ChatFragment()
        }
    }
}