package com.rance.chatui.ui.activity

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

/**
 * author: daxiong
 * created on: 2019-11-25 16:12
 * -----------------------------------------------
 * description:
 * -----------------------------------------------
 */
class AddFriendActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        val etUserName = findViewById<EditText>(R.id.edt_username)
        val etAccount = findViewById<EditText>(R.id.edt_account)
        val btnAdd = findViewById<Button>(R.id.btn_add)
        val btnWait = findViewById<Button>(R.id.btn_add_wait)
        val tvBack =  findViewById<TextView>(R.id.tv_back)
        tvBack.setOnClickListener {
            finish()
        }

            btnAdd.setOnClickListener {
                if(!TextUtils.isEmpty(etUserName.text.toString())){
                    LMClient.addFriend(etUserName.text.toString(), object : LMBasicApiCallback() {
                        override fun gotResultFail(i: Int, s: String) {
                            Toast.makeText(this@AddFriendActivity, s, Toast.LENGTH_LONG).show()

                        }

                        override fun gotResultSuccess() {
                            Toast.makeText(this@AddFriendActivity, "添加成功", Toast.LENGTH_LONG).show()
                        }
                    })
                }else{
                    Toast.makeText(this@AddFriendActivity, "用户名不能为空", Toast.LENGTH_LONG).show()
                }

            }

            btnWait.setOnClickListener {
                if(!TextUtils.isEmpty(etUserName.text.toString())){
                    LMClient.friendRequest(etAccount.text.toString(), object : LMBasicApiCallback() {
                        override fun gotResultSuccess() {
                            Toast.makeText(this@AddFriendActivity, "发送成功", Toast.LENGTH_LONG).show()
                        }

                        override fun gotResultFail(i: Int, s: String) {
                            Toast.makeText(this@AddFriendActivity, s, Toast.LENGTH_LONG).show()
                        }
                    })
                }else{
                    Toast.makeText(this@AddFriendActivity, "用户名不能为空", Toast.LENGTH_LONG).show()

                }

        }


    }





}