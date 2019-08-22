# AndroidLearning
- kotlin-rxjava-MVP 架构
- 集成RxBus（没有STICKY）,感谢[Blankj](https://github.com/Blankj/RxBus)
- 集成RXBinding(version 3.0.0)，感谢[jakeWharton](https://github.com/JakeWharton/RxBinding)
- dependentions{
    'rxJava': 'io.reactivex.rxjava2:rxjava:2.2.10',
    'rxAndroid': 'io.reactivex.rxjava2:rxandroid:2.1.1',
}

- BaseActivity处理好所有的解绑事件{
    mContext置空（mContext = null）
    Rxbus解除注册(Rxbus.unregister(mContext))
}