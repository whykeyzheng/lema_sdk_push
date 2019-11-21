package com.rance.chatui.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lema.imsdk.callback.LMBasicApiCallback
import com.lema.imsdk.client.LMClient
import com.rance.chatui.R

/**
 * author: daxiong
 * created on: 2019-11-20 19:55
 * -----------------------------------------------
 * description:
 * -----------------------------------------------
 */
class PassUpdateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_pass)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }



        val tvOldPass = findViewById<TextView>(R.id.et_old_pass)
        val tvNewPass = findViewById<TextView>(R.id.et_new_pass)
        val tvBack = findViewById<TextView>(R.id.tv_back)
        val button = findViewById<Button>(R.id.btn)


        // 返回上一页
        tvBack.setOnClickListener {
            finish()
        }

        //修改按钮
        button.setOnClickListener {
            val oldPass = tvOldPass.text.toString()
            val newPass = tvNewPass.text.toString()

            if (!TextUtils.isEmpty(oldPass) && !TextUtils.isEmpty(newPass)) {
                LMClient.login(oldPass, newPass, lmcallback)
            } else {
                Toast.makeText(this, "旧密码或新密码不能为空!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    /**
     * 登录回调
     * */
    val lmcallback = object : LMBasicApiCallback() {
        //回调
        override fun gotResultFail(p0: Int, p1: String?) {
            Toast.makeText(this@PassUpdateActivity, "修改密码失败: $p1", Toast.LENGTH_SHORT).show()
        }

        override fun gotResultSuccess() {
            Toast.makeText(this@PassUpdateActivity, "修改密码成功", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}