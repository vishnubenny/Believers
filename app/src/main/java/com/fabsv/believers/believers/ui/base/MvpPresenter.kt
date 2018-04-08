package com.lv.note.personalnote.ui.base

import com.fabsv.believers.believers.ui.base.MvpView

/**
 * Created by vishnu.benny on 2/20/2018.
 */
interface MvpPresenter<V: MvpView> {

    fun attachView(view: V)

    fun detachView(retainInstance: Boolean)
}