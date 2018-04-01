package com.fabsv.believers.believers.data.source.local

import android.content.Context
import com.androidhuman.rxfirebase2.auth.PhoneAuthEvent
import com.fabsv.believers.believers.data.source.UserDataSource
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.util.constants.AppConstants
import com.fabsv.believers.believers.util.methods.RxUtils
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Observable
import io.reactivex.Single

class UserLocalDataSource(val context: Context, val appPreferencesHelper: AppPreferencesHelper) : UserDataSource {
    override fun loginWithPhoneNumber(phoneNumber: String): Observable<Boolean> {
        val status = (AppConstants.LoginConstants.DUMMY_PHONE_NUMBER).equals(phoneNumber)
        return Observable.just(status)
    }

    override fun onLogoutClicked(): Observable<Boolean> {
        appPreferencesHelper.setLoggedIn(false)
        return RxUtils.makeObservable(true)
    }

    override fun getFirebasePhoneAuthObservable(): Observable<PhoneAuthEvent>? {
        return null
    }

    override fun firebaseVerifyOtp(verificationId: String, otpEntered: String): Single<FirebaseUser>? {
        return null
    }
}