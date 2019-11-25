package com.rance.chatui.eventbus

import org.greenrobot.eventbus.EventBus

/**
 * author: daxiong
 * created on: 2019-11-25 19:49
 * -----------------------------------------------
 * description:
 * -----------------------------------------------
 */
class FriendLlistEventBus {
    companion object {
        fun post() {
            EventBus.getDefault().post(FriendLlistEventBus())
        }
    }
}