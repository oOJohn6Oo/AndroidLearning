package com.venpoo.androidlearning.rxbus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.disposables.Disposable;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/12/25
 *     desc  :
 * </pre>
 */
final class CacheUtils {

    private final Map<Object, List<Disposable>> disposablesMap = new ConcurrentHashMap<>();

    private CacheUtils() {

    }

    public static CacheUtils getInstance() {
        return Holder.CACHE_UTILS;
    }

    void addDisposable(Object subscriber, Disposable disposable) {
        synchronized (disposablesMap) {
            List<Disposable> list = disposablesMap.get(subscriber);
            if (list == null) {
                list = new ArrayList<>();
                list.add(disposable);
                disposablesMap.put(subscriber, list);
            } else {
                list.add(disposable);
            }
        }
    }

    void removeDisposables(final Object subscriber) {
        synchronized (disposablesMap) {
            List<Disposable> disposables = disposablesMap.get(subscriber);
            if (disposables == null) return;
            for (Disposable disposable : disposables) {
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                }
            }
            disposables.clear();
            disposablesMap.remove(subscriber);
        }
    }

    private static class Holder {
        private static final CacheUtils CACHE_UTILS = new CacheUtils();
    }
}
