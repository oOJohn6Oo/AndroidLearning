package com.venpoo.androidlearning

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding3.view.clicks
import com.venpoo.androidlearning.base.BaseActivity
import com.venpoo.androidlearning.presenter.RegisterPresenter
import com.venpoo.androidlearning.rxbus.MuseRxBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_reg.*
import java.util.concurrent.TimeUnit

class RegisterActivity :BaseActivity<RegisterPresenter>() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        /**
         * 按钮点击事件
         */
        btn_reg.clicks().throttleFirst(1000,TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                startActivity(Intent(this@RegisterActivity,MainActivity::class.java))
                //测试前一Activity是否能收到消息
                MuseRxBus.get().post("Test","Reg request to destroy login!")
            }
    }

    override fun getPresenter(): RegisterPresenter  = RegisterPresenter(this)
    override fun onDestroy() {
        super.onDestroy()
        Log.d("LQ","RegisterActivity:OnDestroy")
        MuseRxBus.get().unregister(this)
    }
}