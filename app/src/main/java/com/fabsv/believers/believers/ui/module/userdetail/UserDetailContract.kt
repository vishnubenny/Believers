package com.fabsv.believers.believers.ui.module.userdetail

import com.lv.note.personalnote.ui.base.MvpPresenter
import com.fabsv.believers.believers.ui.base.MvpView
import io.reactivex.Observable

interface UserDetailContract {
    interface UserDetailView : MvpView {
        fun getRejectButtonClickEvent(): Observable<Any>
        fun getApproveButtonClickEvent(): Observable<Any>

        fun getScannedQrCode(): String

        fun exitUserDetailScreen()
        fun onApproveStatusUpdateFailed()
        fun onApproveStatusUpdateSuccess()

    }

    interface UserDetailPresenter : MvpPresenter<UserDetailContract.UserDetailView> {
        fun validate()

    }
}