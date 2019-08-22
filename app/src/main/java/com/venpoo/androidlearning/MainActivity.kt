package com.venpoo.androidlearning

import android.annotation.SuppressLint
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding3.widget.editorActionEvents
import com.venpoo.androidlearning.base.BaseActivity
import com.venpoo.androidlearning.presenter.MainPresenter
import com.venpoo.androidlearning.rxbus.MuseRxBus
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

open class MainActivity : BaseActivity<MainPresenter>() {

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //测试STICKY功能（没有subscribe的情况下能不能收到）
        MuseRxBus.get().post("Test","Main test1: STICKY")
        url.editorActionEvents()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { action ->
                if (action.actionId == EditorInfo.IME_ACTION_SEARCH||(action.keyEvent?.keyCode==KeyEvent.KEYCODE_ENTER && action.keyEvent?.action==KeyEvent.ACTION_UP)) {
                    if (!url.text.toString().startsWith("https://") &&
                        !url.text.toString().startsWith("http://")
                    )
                        browser.loadUrl("https://${url.text}")
                    else
                        browser.loadUrl(url.text.toString())
                }
                //测试Reg页面是否正常，Login页面是否正常回收
                MuseRxBus.get().post("Test","Main test again")
            }

        MuseRxBus.get().subscribe(this,"Test",Schedulers.newThread(),object :MuseRxBus.Callback<String>(){
            override fun onReceive(t: String?) {
                Log.d("LQ", "Main test2:---$t")
            }
        })

        browser.settings.domStorageEnabled = true
        browser.settings.javaScriptEnabled = true
        browser.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
        }

    }

    override fun getPresenter(): MainPresenter = MainPresenter(this)
}
