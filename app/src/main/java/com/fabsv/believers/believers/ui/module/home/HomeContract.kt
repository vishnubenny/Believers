package com.fabsv.believers.believers.ui.module.home

import com.lv.note.personalnote.ui.base.MvpPresenter
import com.lv.note.personalnote.ui.base.MvpView
import io.reactivex.Observable

interface HomeContract {

    interface HomeView : MvpView {
        fun getLogoutButtonClickEvent(): Observable<Any>
        fun getScanButtonClickEvent(): Observable<Any>
        fun showLoggedInUserPhoneNumber(phoneNumber: String)

    }

    interface HomePresenter : MvpPresenter<HomeContract.HomeView> {
        fun validate()
        fun showLoggedInUserDetail()

    }
}