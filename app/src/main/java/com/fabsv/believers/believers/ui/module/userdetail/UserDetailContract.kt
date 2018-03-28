package com.fabsv.believers.believers.ui.module.userdetail

import com.lv.note.personalnote.ui.base.MvpPresenter
import com.lv.note.personalnote.ui.base.MvpView
import io.reactivex.Observable

interface UserDetailContract {
    interface UserDetailView : MvpView {
        fun getRejectButtonClickEvent(): Observable<Any>
        fun exitUserDetailScreen()

    }

    interface UserDetailPresenter : MvpPresenter<UserDetailContract.UserDetailView> {
        fun validate()

    }
}