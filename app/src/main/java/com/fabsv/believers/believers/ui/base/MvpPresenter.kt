package com.lv.note.personalnote.ui.base

/**
 * Created by vishnu.benny on 2/20/2018.
 */
interface MvpPresenter<V: MvpView> {

    fun attachView(view: V)

    fun detachView(retainInstance: Boolean)
}