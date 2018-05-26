package com.fabsv.believers.believers.ui.module.scan

import com.jakewharton.rxbinding2.InitialValueObservable
import com.lv.note.personalnote.ui.base.MvpPresenter
import com.fabsv.believers.believers.ui.base.MvpView
import io.reactivex.Observable

interface ScanContract {

    interface ScanView : MvpView {
        fun getScanAgainButtonClickEvent(): Observable<Any>
        fun getSubmitButtonClickEvent(): Observable<Boolean>
        fun getScanFieldTextChanges(): InitialValueObservable<CharSequence>
        fun resetScanCameraView()
        fun updateSubmitButtonState(isEnable: Boolean)
        fun getQrCodeFieldValue(): String
        fun resetScanScreen()
    }

    interface ScanPresenter : MvpPresenter<ScanContract.ScanView> {
        fun validate()
        fun unSubscribeValidations()

    }
}