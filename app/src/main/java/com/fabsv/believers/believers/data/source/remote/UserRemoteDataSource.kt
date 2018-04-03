package com.fabsv.believers.believers.data.source.remote

import android.app.Activity
import android.content.Context
import com.androidhuman.rxfirebase2.auth.PhoneAuthEvent
import com.androidhuman.rxfirebase2.auth.RxPhoneAuthProvider
import com.fabsv.believers.believers.data.source.UserDataSource
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.data.source.remote.model.User
import com.fabsv.believers.believers.data.source.remote.retrofit.ApiClient
import com.fabsv.believers.believers.data.source.remote.retrofit.ApiInterface
import com.fabsv.believers.believers.util.methods.RxUtils
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.Observable
import org.jetbrains.anko.AnkoLogger
import java.util.concurrent.TimeUnit

class UserRemoteDataSource(val context: Context, val appPreferencesHelper: AppPreferencesHelper) :
        UserDataSource, AnkoLogger {

    override fun loginWithPhoneNumber(phoneNumber: String): Observable<Boolean> {
        val apiInterface: ApiInterface = ApiClient.getClient()!!.create(ApiInterface::class.java)
        return apiInterface.userLogin(phoneNumber)
                .map { user: User -> "y".contentEquals(user.getStatus()!!) }
    }

    override fun onLogoutClicked(): Observable<Boolean> {
        return RxUtils.makeObservable(false)
    }

    override fun getFirebasePhoneAuthObservable(phoneNumberFieldValue: String): Observable<PhoneAuthEvent>? {
        val phoneAuthProvider = PhoneAuthProvider.getInstance()
        return RxPhoneAuthProvider.verifyPhoneNumber(phoneAuthProvider, "+91$phoneNumberFieldValue", 5,
                TimeUnit.SECONDS, context as Activity)
    }
}