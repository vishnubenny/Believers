package com.fabsv.believers.believers.ui.module.login

import com.jakewharton.rxbinding2.InitialValueObservable
import com.lv.note.personalnote.ui.base.MvpPresenter
import com.fabsv.believers.believers.ui.base.MvpView
import io.reactivex.Observable

interface LoginContract {
    interface LoginView : MvpView {
        fun getPhoneNumberField(): InitialValueObservable<CharSequence>
        fun updateLoginButtonStatus(enable: Boolean)
        fun getLoginButtonClick(): Observable<Any>
        fun getPhoneNumberFieldValue(): String
        fun onLoginSuccess()
        fun onLoginFailure()
        fun resetScreen()

    }

    interface LoginPresenter : MvpPresenter<LoginContract.LoginView> {
        fun validate()
        fun showHomeFragment()
        fun unSubscribeValidations()

    }
}