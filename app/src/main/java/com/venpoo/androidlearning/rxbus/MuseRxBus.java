package com.venpoo.androidlearning.rxbus;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;


public final class MuseRxBus {

    private final FlowableProcessor<Object> mBus;

    private final Consumer<Throwable> mOnError = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) {
            Utils.logE(throwable.toString());
        }
    };

    private MuseRxBus() {
        mBus = PublishProcessor.create().toSerialized();
    }

    public static MuseRxBus get() {
        return Holder.BUS;
    }

    public void post(final String tag,final Object event) {
        Utils.requireNonNull(event, tag);
        TagMessage msgEvent = new TagMessage(event, tag);
        mBus.onNext(msgEvent);
    }

    public <T> void subscribe(final Object subscriber,
                               final String tag,
                               final Scheduler scheduler,
                               final Callback<T> callback) {
        Utils.requireNonNull(subscriber, tag, callback);

        final Class<T> typeClass = Utils.getTypeClassFromParadigm(callback);

        final Consumer<T> onNext = new Consumer<T>() {
            @Override
            public void accept(T t) {
                callback.onReceive(t);
            }
        };

        Disposable disposable = FlowableUtils.subscribe(
                toFlowable(typeClass, tag, scheduler), onNext, mOnError);
        CacheUtils.getInstance().addDisposable(subscriber, disposable);
    }

    private <T> Flowable<T> toFlowable(final Class<T> eventType,
                                       final String tag,
                                       final Scheduler scheduler) {
        Flowable<T> flowable = mBus.ofType(TagMessage.class)
                .filter(new Predicate<TagMessage>() {
                    @Override
                    public boolean test(TagMessage tagMessage) {
                        return tagMessage.isSameType(eventType, tag);
                    }
                })
                .map(new Function<TagMessage, Object>() {
                    @Override
                    public Object apply(TagMessage tagMessage) {
                        return tagMessage.mEvent;
                    }
                })
                .cast(eventType);
        if (scheduler != null) {
            return flowable.observeOn(scheduler);
        }
        return flowable;
    }

    public void unregister(final Object subscriber) {
        CacheUtils.getInstance().removeDisposables(subscriber);
    }

    private static class Holder {
        private static final MuseRxBus BUS = new MuseRxBus();
    }

    public abstract static class Callback<T> {
        public abstract void onReceive(T t);
    }
}