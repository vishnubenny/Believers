package com.fabsv.believers.believers.data.repository

import android.content.Context
import com.androidhuman.rxfirebase2.auth.PhoneAuthEvent
import com.fabsv.believers.believers.data.source.local.UserLocalDataSource
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.data.source.remote.UserRemoteDataSource
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single

class UserRepository(private val context: Context, val appPreferencesHelper: AppPreferencesHelper) {

    private var userLocalDataSource: UserLocalDataSource
    private var userRemoteDataSource: UserRemoteDataSource

    init {
        this.userLocalDataSource = UserLocalDataSource(context, appPreferencesHelper)
        this.userRemoteDataSource = UserRemoteDataSource(context, appPreferencesHelper)
    }

    fun loginWithPhoneNumber(phoneNumber: String): Observable<Boolean> {
        return userLocalDataSource.loginWithPhoneNumber(phoneNumber)
    }

    fun onLogoutClicked(): Observable<Boolean> {
        return userLocalDataSource.onLogoutClicked()
    }

    fun getFirebasePhoneAuthObservable(): Observable<PhoneAuthEvent>? {
        return userRemoteDataSource.getFirebasePhoneAuthObservable()
    }

    fun firebaseVerifyOtp(verificationId: String, otpEntered: String): Single<FirebaseUser>? {
        return userRemoteDataSource.firebaseVerifyOtp(verificationId, otpEntered)
    }
}