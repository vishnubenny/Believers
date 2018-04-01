package com.fabsv.believers.believers.ui.module.login

import android.content.Context
import com.androidhuman.rxfirebase2.auth.PhoneAuthEvent
import com.fabsv.believers.believers.data.repository.UserRepository
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Observable
import io.reactivex.Single

class LoginInteractor(private val context: Context, val appPreferencesHelper: AppPreferencesHelper) {

    private var userRepository: UserRepository

    fun loginWithPhoneNumber(phoneNumber: String): Observable<Boolean> {
        return userRepository.loginWithPhoneNumber(phoneNumber)
    }

    fun getFirebasePhoneAuthObservable(): Observable<PhoneAuthEvent>? {
        return userRepository.getFirebasePhoneAuthObservable()
    }

    fun firebaseVerifyOtp(verificationId: String, otpEntered: String): Single<FirebaseUser>? {
        return userRepository.firebaseVerifyOtp(verificationId, otpEntered)
    }

    init {
        this.userRepository = UserRepository(context, appPreferencesHelper)
    }
}