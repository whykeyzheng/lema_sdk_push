package com.rance.chatui.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.lema.imsdk.bean.LMUserBean
import com.lema.imsdk.callback.LMBasicBeanCallback
import com.lema.imsdk.client.LMClient
import com.rance.chatui.R

/**
 * author: daxiong
 * created on: 2019-11-20 16:35
 * -----------------------------------------------
 * description:
 * -----------------------------------------------
 */
class UpdateUserInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user_info)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        val etName = findViewById<EditText>(R.id.et_name)
        val etNickName = findViewById<EditText>(R.id.et_nickname)
        val etBirthday = findViewById<EditText>(R.id.et_birthday)
        val etSignature = findViewById<EditText>(R.id.et_signature)
        val spGender = findViewById<Spinner>(R.id.spinner_gender)
        val etRegion = findViewById<EditText>(R.id.et_region)
        val etAddress = findViewById<EditText>(R.id.et_address)
        val button = findViewById<Button>(R.id.btn)

        var gen = 0

        val myuser = LMClient.getMyUser()
        if (myuser != null) {
            etName.setText(myuser.username)
            etNickName.setText(myuser.nickname)
            etBirthday.setText(myuser.birthday)
            etSignature.setText(myuser.signature)
            //  etGender.setText(if (myuser.gender == 0) "男" else "女")
            spGender.setSelection(myuser.gender)
            etRegion.setText(myuser.region)
            etAddress.setText(myuser.address)
        }

        spGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //  val s = resources.getStringArray(R.array.study_view_spinner_values)[position]
                gen = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        button.setOnClickListener {
            val map = HashMap<LMUserBean.Field, String>()
            map[LMUserBean.Field.nickname] = etNickName.text.toString()
            map[LMUserBean.Field.birthday] = "2019-10-11"
            map[LMUserBean.Field.signature] = etSignature.text.toString()
            map[LMUserBean.Field.gender] = gen.toString()
            map[LMUserBean.Field.region] = etRegion.text.toString()
            map[LMUserBean.Field.address] = etAddress.text.toString()

            LMClient.userInfoUpdata(map, lmcallback)
        }

    }

    /**
     * 修改回调
     * */
    val lmcallback = object : LMBasicBeanCallback<LMUserBean>() {
        override fun gotResultFail(p0: Int, p1: String?) {
            Toast.makeText(this@UpdateUserInfoActivity, "修改失败: $p1", Toast.LENGTH_SHORT).show()

        }

        override fun gotResultSuccess(p0: LMUserBean?) {
            Toast.makeText(this@UpdateUserInfoActivity, "修改成功", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

}