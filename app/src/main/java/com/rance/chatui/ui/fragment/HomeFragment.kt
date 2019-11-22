package com.rance.chatui.ui.fragment

import android.content.Intent
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.lema.imsdk.client.LMClient
import com.lema.imsdk.util.LMLogUtils

import com.rance.chatui.R
import com.rance.chatui.eventbus.UserInfoEventBus
import com.rance.chatui.ui.activity.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class HomeFragment : Fragment() {

    val tvName by lazy { view!!.findViewById<TextView>(R.id.tv_name) }
    val ivAvatar by lazy { view!!.findViewById<ImageView>(R.id.iv_avatar) }
    val tvNickName by lazy { view!!.findViewById<TextView>(R.id.tv_nickname) }
    val tvBirthday by lazy { view!!.findViewById<TextView>(R.id.tv_birthday) }
    val tvSignature by lazy { view!!.findViewById<TextView>(R.id.tv_signature) }
    val tvGender by lazy { view!!.findViewById<TextView>(R.id.tv_gender) }
    val tvRegion by lazy { view!!.findViewById<TextView>(R.id.tv_region) }
    val tvAddress by lazy { view!!.findViewById<TextView>(R.id.tv_address) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvUpdateAvatar = view.findViewById<TextView>(R.id.tv_update_avatar)
        val tvUserInfo = view.findViewById<TextView>(R.id.tv_update_user_info)

        showUserInfo()

        //修改头像
        tvUpdateAvatar.setOnClickListener {
            val intent = Intent(context, AvatarLoadActivity::class.java)
            startActivity(intent)
        }

        //修改个人资料
        tvUserInfo.setOnClickListener {
            val intent = Intent(context, UpdateUserInfoActivity::class.java)
            startActivity(intent)
        }
    }


    fun showUserInfo() {
        //获取当前登录账号的用户信息
        val myuser = LMClient.getMyUser()
        LMLogUtils.d("daxiong", "==== LMClient.getMyUser()====" + myuser)
        //展示数据
        if (myuser != null) {
            Glide.with(context!!).load(myuser.avatar_url).placeholder(R.mipmap.ic_start_logo).error(R.mipmap.ic_start_logo).into(ivAvatar)
            tvName.text = myuser.username
            tvNickName.text = myuser.nickname
            tvBirthday.text = myuser.birthday
            tvSignature.text = myuser.signature
            tvGender.text = if (myuser.gender == 0) "男" else "女"
            tvRegion.text = myuser.region
            tvAddress.text = myuser.address

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUserInfoEvent(event: UserInfoEventBus) {//接收
        showUserInfo()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

}
