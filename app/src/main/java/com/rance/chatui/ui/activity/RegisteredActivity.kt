package com.rance.chatui.ui.activity

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lema.imsdk.bean.LMUserBean
import com.lema.imsdk.callback.LMBasicApiCallback
import com.lema.imsdk.client.LMClient
import com.rance.chatui.R

/**
 * author: daxiong
 * created on: 2019-11-20 15:31
 * -----------------------------------------------
 * description: 注册页
 * -----------------------------------------------
 */
class RegisteredActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registered)
        if (supportActionBar != null){
            supportActionBar!!.hide()
        }

        val etName = findViewById<EditText>(R.id.et_name)
        val etPass = findViewById<EditText>(R.id.et_pass)
        val button = findViewById<Button>(R.id.btn)
        val tvLogin = findViewById<TextView>(R.id.tv_login)

        val map :Map<LMUserBean.Field,String> = mapOf()
        //注册
        button.setOnClickListener {
            val name = etName.text.toString()
            val pass = etPass.text.toString()

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass)) {
                LMClient.register(name,pass,map,lmcallback)
            } else {
                Toast.makeText(this, "要注册的账号或密码不能为空!", Toast.LENGTH_SHORT).show()
            }
        }
        //返回登录页
        tvLogin.setOnClickListener {
            finish()
        }
    }
    /**
     * 注册回调
     * */
    val lmcallback = object : LMBasicApiCallback() {
        override fun gotResultFail(p0: Int, p1: String?) {
            //失败
            Toast.makeText(this@RegisteredActivity, "注册失败:" + p1, Toast.LENGTH_LONG).show()
        }

        override fun gotResultSuccess() {
            //成功
            Toast.makeText(this@RegisteredActivity, "注册成功,自动返回登录页", Toast.LENGTH_LONG).show()
            finish()
        }
    }

}