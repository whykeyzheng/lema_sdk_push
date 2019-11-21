package com.rance.chatui.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lema.imsdk.client.LMClient
import com.lema.imsdk.util.LMLogUtils
import com.rance.chatui.R

/**
 * author: daxiong
 * created on: 2019-11-20 16:35
 * -----------------------------------------------
 * description:
 * -----------------------------------------------
 */
class MessageListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val myuser = LMClient.getMyUser()
        LMLogUtils.d("daxiong", "=======" + myuser)

    }

}