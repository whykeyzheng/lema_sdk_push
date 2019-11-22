package com.rance.chatui.eventbus

import org.greenrobot.eventbus.EventBus

/**
 * author: daxiong
 * created on: 2019-11-22 11:57
 * -----------------------------------------------
 * description:
 * -----------------------------------------------
 */
class UserInfoEventBus {
    companion object {
        fun post() {
        EventBus.getDefault().post(UserInfoEventBus())
        }
    }

}