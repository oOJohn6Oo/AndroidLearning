package com.venpoo.androidlearning.presenter

import android.content.Context
import com.venpoo.androidlearning.base.BasePresenter
import com.venpoo.androidlearning.rxbus.MuseRxBus

class LoginPresenter(context: Context) :BasePresenter(context) {

    override fun unsubscribe() {
        MuseRxBus.get().unregister(mContext)
        mContext = null
    }
}