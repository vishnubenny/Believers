package com.fabsv.believers.believers.ui.module.login

import android.content.Context
import com.androidhuman.rxfirebase2.auth.PhoneAuthEvent
import com.fabsv.believers.believers.data.repository.UserRepository
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.data.source.remote.model.LoginRequest
import io.reactivex.Observable

class LoginInteractor(private val context: Context, val appPreferencesHelper: AppPreferencesHelper) {

    private var userRepository: UserRepository = UserRepository(context, appPreferencesHelper)

    fun loginWithPhoneNumber(loginRequest: LoginRequest): Observable<Boolean> {
        return userRepository.loginWithPhoneNumber(loginRequest)
    }

    fun getFirebasePhoneAuthObservable(phoneNumberFieldValue: String): Observable<PhoneAuthEvent>? {
        return userRepository.getFirebasePhoneAuthObservable(phoneNumberFieldValue)
    }

}