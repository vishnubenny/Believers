package com.lv.note.personalnote.ui.base

import java.lang.ref.WeakReference

/**
 * Created by vishnu.benny on 2/20/2018.
 */
open class MvpBasePresenter<V : MvpView> : MvpPresenter<V> {
    private var viewRef: WeakReference<V>? = null

    override fun attachView(view: V) {
        viewRef = WeakReference(view)
    }

    fun getView(): V? {
        return if (null != viewRef) viewRef!!.get()
        else null
    }

    fun isViewAttached(): Boolean {
        return null != viewRef && null != viewRef!!.get();
    }

    override fun detachView(retainInstance: Boolean) {
        if (null != viewRef) {
            viewRef!!.clear()
            viewRef = null
        }
    }
}