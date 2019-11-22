package com.rance.chatui.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.lema.imsdk.client.LMClient
import com.rance.chatui.R
import com.rance.chatui.ui.activity.AvatarLoadActivity
import com.rance.chatui.ui.activity.MainActivity
import com.rance.chatui.ui.activity.PassUpdateActivity

/**
 * author: daxiong
 * created on: 2019-11-20 16:34
 * -----------------------------------------------
 * description:
 * -----------------------------------------------
 */
class UserInfoFragment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        val ivAvatar = findViewById<ImageView>(R.id.iv_avatar)

        val tvName = findViewById<TextView>(R.id.tv_name)
        val tvNickName = findViewById<TextView>(R.id.tv_nickname)
        val tvBirthday = findViewById<TextView>(R.id.tv_birthday)
        val tvSignature = findViewById<TextView>(R.id.tv_signature)
        val tvGender = findViewById<TextView>(R.id.tv_gender)
        val tvRegion = findViewById<TextView>(R.id.tv_region)
        val tvAddress = findViewById<TextView>(R.id.tv_address)

        val tvUpdatePass = findViewById<TextView>(R.id.tv_update_pass)
        val tvUpdateAvatar = findViewById<TextView>(R.id.tv_update_avatar)
        val tvChatMain = findViewById<TextView>(R.id.tv_chat_main)

        //获取当前登录账号的用户信息
        val myuser = LMClient.getMyUser()

        //展示数据
        if (myuser != null) {
            tvName.text = myuser.username
            tvNickName.text = myuser.nickname
            tvBirthday.text = myuser.birthday
            tvSignature.text = myuser.signature
            tvGender.text = if (myuser.gender == 0) "男" else "女"
            tvRegion.text = myuser.region
            tvAddress.text = myuser.address

        }
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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }


}