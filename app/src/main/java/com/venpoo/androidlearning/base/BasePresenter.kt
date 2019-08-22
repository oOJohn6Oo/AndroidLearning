package com.venpoo.androidlearning.base

import android.content.Context

abstract class BasePresenter(context: Context) {
    var mContext :Context? = context
    abstract fun unsubscribe()
}