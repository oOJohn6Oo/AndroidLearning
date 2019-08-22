package com.venpoo.androidlearning

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.jakewharton.rxbinding3.view.clicks
import com.venpoo.androidlearning.base.BaseActivity
import com.venpoo.androidlearning.presenter.LoginPresenter
import com.venpoo.androidlearning.rxbus.MuseRxBus
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit

class LoginActivity : BaseActivity<LoginPresenter>(){
    val s = "123"
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        val o = btn_login.clicks().throttleFirst(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                startActivity(Intent().setClass(this, RegisterActivity::class.java))
            }

        MuseRxBus.get().subscribe(this,"Test",AndroidSchedulers.mainThread()
            ,object : MuseRxBus.Callback<String>(){
                override fun onReceive(t: String) {
                    Log.d("LQ", "LoginActivity---$t")
                    finish()
                }
            })
    }

    override fun getPresenter(): LoginPresenter  = LoginPresenter(this)

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LQ", "Login:onDestroy")
    }
}