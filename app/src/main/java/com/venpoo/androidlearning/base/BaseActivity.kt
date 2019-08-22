package com.venpoo.androidlearning.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

abstract class  BaseActivity<T:BasePresenter> :AppCompatActivity() {

    lateinit var mPresenter : BasePresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = getPresenter()
    }

    abstract fun getPresenter(): T

    override fun onDestroy() {
        mPresenter.unsubscribe()
        super.onDestroy()
        Log.d("LQ",javaClass.simpleName+Thread.currentThread().stackTrace[3].methodName)
    }
}