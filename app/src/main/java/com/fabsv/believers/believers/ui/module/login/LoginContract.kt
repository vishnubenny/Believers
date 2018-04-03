package com.fabsv.believers.believers.ui.module.login

import com.fabsv.believers.believers.ui.base.MvpView
import com.jakewharton.rxbinding2.InitialValueObservable
import com.lv.note.personalnote.ui.base.MvpPresenter
import io.reactivex.Observable
import io.reactivex.ObservableSource

interface LoginContract {
    interface LoginView : MvpView {
        fun getPhoneNumberField(): InitialValueObservable<CharSequence>
        fun updateLoginButtonStatus(enable: Boolean)
        fun updateVerifyOtpButtonStatus(isValidOtp: Boolean)
        fun updateVerifyAuthCodeLayoutStatus(showVerifyLayout: Boolean)
        fun updateOtpAttemptFailRetryLayoutStatus(isShowOtpRetryLayout: Boolean)
        fun getLoginButtonClick(): Observable<Any>
        fun getVerifyOtpButtonClick(): Observable<Any>
        fun getRetryButtonClick(): Observable<Any>
        fun getChangeNumberButtonClick(): Observable<Any>
        fun getPhoneNumberFieldValue(): String
        fun getVerifyOtpFieldValue(): String
        fun onLoginSuccess()
        fun onLoginFailure()
        fun resetScreen()

    }

    interface LoginPresenter : MvpPresenter<LoginContract.LoginView> {
        fun validate()
        fun showHomeFragment()
        fun updateVerifyAuthCodeLayoutStatus(showVerifyLayout: Boolean)
        fun updateOtpAttemptFailRetryLayoutStatus(isShowOtpRetryLayout: Boolean)
        fun onPhoneAuthVerificationCompleteEvent(): Boolean
        fun resetScreen()
        fun unSubscribeValidations()

    }
}