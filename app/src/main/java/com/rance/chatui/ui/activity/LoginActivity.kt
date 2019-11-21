package com.rance.chatui.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lema.imsdk.callback.LMBasicApiCallback
import com.lema.imsdk.client.LMClient
import com.rance.chatui.R

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        val etName = findViewById<EditText>(R.id.et_name)
        val etPass = findViewById<EditText>(R.id.et_pass)
        val button = findViewById<Button>(R.id.btn)
        val tvRegistered = findViewById<TextView>(R.id.tv_registered)


        //初始化
        LMClient.init(this)

        //登录
        button.setOnClickListener {
            val name = etName.text.toString()
            val pass = etPass.text.toString()

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass)) {

                LMClient.login(name, pass, lmcallback)
            } else {
                Toast.makeText(this, "账号或密码不能为空!", Toast.LENGTH_SHORT).show()
            }
        }

        //注册
        tvRegistered.setOnClickListener {
            val intent = Intent(this, RegisteredActivity::class.java)
            startActivity(intent)
        }


    }

    /**
     * 登录回调
     * */
    val lmcallback = object : LMBasicApiCallback() {
        //回调
        override fun gotResultFail(p0: Int, p1: String?) {
            Toast.makeText(this@LoginActivity, "登录失败: $p1", Toast.LENGTH_SHORT).show()
        }

        override fun gotResultSuccess() {
            Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@LoginActivity, UserInfoActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}
