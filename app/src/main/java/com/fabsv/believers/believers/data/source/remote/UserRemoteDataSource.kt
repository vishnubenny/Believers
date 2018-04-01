package com.fabsv.believers.believers.data.source.remote

import android.app.Activity
import android.content.Context
import com.androidhuman.rxfirebase2.auth.PhoneAuthEvent
import com.androidhuman.rxfirebase2.auth.RxPhoneAuthProvider
import com.androidhuman.rxfirebase2.auth.rxSignInWithCredential
import com.fabsv.believers.believers.data.source.UserDataSource
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.data.source.remote.model.User
import com.fabsv.believers.believers.data.source.remote.retrofit.ApiClient
import com.fabsv.believers.believers.data.source.remote.retrofit.ApiInterface
import com.fabsv.believers.believers.util.methods.RxUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.Observable
import io.reactivex.Single
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

    override fun getFirebasePhoneAuthObservable(): Observable<PhoneAuthEvent>? {
        val phoneAuthProvider = PhoneAuthProvider.getInstance()
        return RxPhoneAuthProvider.verifyPhoneNumber(phoneAuthProvider, "+919744234506", 120,
                TimeUnit.SECONDS, context as Activity)
    }

    override fun firebaseVerifyOtp(verificationId: String, otpEntered: String): Single<FirebaseUser>? {
        val credential = PhoneAuthProvider.getCredential(verificationId, otpEntered)
        return FirebaseAuth.getInstance().rxSignInWithCredential(credential)
    }
}