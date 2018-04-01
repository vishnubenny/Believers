package com.fabsv.believers.believers.data.source

import com.androidhuman.rxfirebase2.auth.PhoneAuthEvent
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Observable
import io.reactivex.Single

interface UserDataSource {
    fun loginWithPhoneNumber(phoneNumber: String): Observable<Boolean>
    fun onLogoutClicked(): Observable<Boolean>
    fun getFirebasePhoneAuthObservable(): Observable<PhoneAuthEvent>?
}