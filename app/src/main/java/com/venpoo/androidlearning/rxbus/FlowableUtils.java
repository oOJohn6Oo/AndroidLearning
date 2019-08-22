package com.venpoo.androidlearning.rxbus;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.operators.flowable.FlowableInternalHelper;


final class FlowableUtils {
    static <T> Disposable subscribe(Flowable<T> flowable,
                                    Consumer<? super T> onNext,
                                    Consumer<? super Throwable> onError) {
        ObjectHelper.requireNonNull(flowable, "flowable is null");
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(onError, "onError is null");

        MyLambdaSubscriber<T> ls = new MyLambdaSubscriber<T>(onNext, onError, Functions.EMPTY_ACTION,
                FlowableInternalHelper.RequestMax.INSTANCE);
        flowable.subscribe(ls);
        return ls;
    }
}
