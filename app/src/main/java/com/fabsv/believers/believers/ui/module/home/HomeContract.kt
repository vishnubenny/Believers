package com.fabsv.believers.believers.ui.module.home

import com.lv.note.personalnote.ui.base.MvpPresenter
import com.fabsv.believers.believers.ui.base.MvpView
import io.reactivex.Observable

interface HomeContract {

    interface HomeView : MvpView {
        fun resetScreen()
        fun getLogoutButtonClickEvent(): Observable<Any>
        fun getScanButtonClickEvent(): Observable<Any>
        fun getCollectionReportButtonClickEvent(): Observable<Boolean>
        fun getQuorumReportButtonClickEvent(): Observable<Boolean>
        fun showLoggedInUsername(phoneNumber: String)

    }

    interface HomePresenter : MvpPresenter<HomeContract.HomeView> {
        fun validate()
        fun showLoggedInUserDetail()
        fun unSubscribeValidations()
    }
}