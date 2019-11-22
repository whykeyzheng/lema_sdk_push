package com.rance.chatui.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.lema.imsdk.client.LMClient
import com.lema.imsdk.util.LMLogUtils
import com.rance.chatui.R
import com.rance.chatui.eventbus.UserInfoEventBus
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * author: daxiong
 * created on: 2019-11-20 16:34
 * -----------------------------------------------
 * description:
 * -----------------------------------------------
 */
class UserInfoActivity : AppCompatActivity() {

    val tvName by lazy { findViewById<TextView>(R.id.tv_name) }
    val ivAvatar by lazy { findViewById<ImageView>(R.id.iv_avatar) }
    val tvNickName by lazy { findViewById<TextView>(R.id.tv_nickname) }
    val tvBirthday by lazy { findViewById<TextView>(R.id.tv_birthday) }
    val tvSignature by lazy { findViewById<TextView>(R.id.tv_signature) }
    val tvGender by lazy { findViewById<TextView>(R.id.tv_gender) }
    val tvRegion by lazy { findViewById<TextView>(R.id.tv_region) }
    val tvAddress by lazy { findViewById<TextView>(R.id.tv_address) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        EventBus.getDefault().register(this)

        val tvUpdatePass = findViewById<TextView>(R.id.tv_update_pass)
        val tvUpdateAvatar = findViewById<TextView>(R.id.tv_update_avatar)
        val tvChatMain = findViewById<TextView>(R.id.tv_chat_main)
        val tvUserInfo = findViewById<TextView>(R.id.tv_update_user_info)
        val tvOutLogin = findViewById<TextView>(R.id.tv_out_log)

        showUserInfo()

        //修改密码
        tvUpdatePass.setOnClickListener {
            val intent = Intent(this, PassUpdateActivity::class.java)
            startActivity(intent)
        }
        //修改头像
        tvUpdateAvatar.setOnClickListener {
            val intent = Intent(this, AvatarLoadActivity::class.java)
            startActivity(intent)
        }

        //聊天主页
        tvChatMain.setOnClickListener {
            val intent = Intent(this, IMActivity::class.java)
            startActivity(intent)
        }
        //退出登录
        tvOutLogin.setOnClickListener {
            LMClient.logout()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        //修改个人资料
        tvUserInfo.setOnClickListener {
            val intent = Intent(this, UpdateUserInfoActivity::class.java)
            startActivity(intent)
        }


    }

    fun showUserInfo() {
        //获取当前登录账号的用户信息
        val myuser = LMClient.getMyUser()
        LMLogUtils.d("daxiong", "==== LMClient.getMyUser()====" + myuser)
        //展示数据
        if (myuser != null) {
            Glide.with(this).load(myuser.avatar_url).placeholder(R.mipmap.ic_start_logo).error(R.mipmap.ic_start_logo).into(ivAvatar)
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


}