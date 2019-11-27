package com.rance.chatui.record

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.rance.chatui.R

/**
 * author: daxiong
 * created on: 2019-11-26 19:26
 * -----------------------------------------------
 * description:
 * -----------------------------------------------
 */
class CommonDialog(context: Context, var title: String, var text: String, var block: ((action: Boolean) -> Unit)? = null) : Dialog(context, R.style.PayTypeDialogStyle) {
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_common_layout, null)
        setContentView(view)
        val window = window
        if (window != null) {
            window.decorView.setPadding(0, 0, 0, 0)
            val lp = window.attributes
            val d = window.windowManager.defaultDisplay // 获取屏幕宽、高度
            lp.width = (d.width * 0.8).toInt()
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = lp
        }
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val tvContent = findViewById<TextView>(R.id.tv_content)
        tvTitle.text = title
        tvContent.text = text

        view.findViewById<TextView>(R.id.tv_confirm).setOnClickListener {
            block?.invoke(true)
            dismiss()

        }
        view.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            block?.invoke(false)
            dismiss()
        }

    }
}