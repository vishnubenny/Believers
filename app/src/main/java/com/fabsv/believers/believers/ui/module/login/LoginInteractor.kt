package com.fabsv.believers.believers.ui.module.login

import android.content.Context
import com.androidhuman.rxfirebase2.auth.PhoneAuthEvent
import com.fabsv.believers.believers.data.repository.UserRepository
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import io.reactivex.Observable

class LoginInteractor(private val context: Context, val appPreferencesHelper: AppPreferencesHelper) {

    private var userRepository: UserRepository

    fun loginWithPhoneNumber(phoneNumber: String): Observable<Boolean> {
        return userRepository.loginWithPhoneNumber(phoneNumber)
    }

    fun getFirebasePhoneAuthObservable(): Observable<PhoneAuthEvent>? {
        return userRepository.getFirebasePhoneAuthObservable()
    }

    init {
        this.userRepository = UserRepository(context, appPreferencesHelper)
    }
}