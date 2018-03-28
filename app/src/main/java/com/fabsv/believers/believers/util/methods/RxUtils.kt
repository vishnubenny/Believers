package com.fabsv.believers.believers.util.methods

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import java.util.concurrent.Callable

class RxUtils {
    companion object {
        fun <T> makeObservable(func: Callable<T>): Observable<T> {
            return Observable.create { e: ObservableEmitter<T> ->
                run {
                    e.onNext(func.call())
                    e.onComplete()
                }
            }
        }

        fun <T> makeObservable(t: T): Observable<T> {
            return Observable.create { e: ObservableEmitter<T> ->
                e.onNext(t)
                e.onComplete()
            }
        }
    }
}