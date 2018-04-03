package com.fabsv.believers.believers.data.source.local

import android.content.Context
import com.androidhuman.rxfirebase2.auth.PhoneAuthEvent
import com.fabsv.believers.believers.data.source.UserDataSource
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.util.constants.AppConstants
import com.fabsv.believers.believers.util.methods.RxUtils
import io.reactivex.Observable

class UserLocalDataSource(val context: Context, val appPreferencesHelper: AppPreferencesHelper) : UserDataSource {
    override fun loginWithPhoneNumber(phoneNumber: String): Observable<Boolean> {
        val status = (AppConstants.LoginConstants.DUMMY_PHONE_NUMBER).equals(phoneNumber) ||
                (AppConstants.LoginConstants.DUMMY_PHONE_NUMBER_1).equals(phoneNumber) ||
                (AppConstants.LoginConstants.DUMMY_PHONE_NUMBER_2).equals(phoneNumber)
        return Observable.just(status)
    }

    override fun onLogoutClicked(): Observable<Boolean> {
        appPreferencesHelper.setLoggedIn(false)
        return RxUtils.makeObservable(true)
    }

    override fun getFirebasePhoneAuthObservable(phoneNumberFieldValue: String): Observable<PhoneAuthEvent>? {
        return null
    }
}