package com.fabsv.believers.believers.data.source

import com.androidhuman.rxfirebase2.auth.PhoneAuthEvent
import io.reactivex.Observable

interface UserDataSource {
    fun loginWithPhoneNumber(phoneNumber: String): Observable<Boolean>
    fun onLogoutClicked(): Observable<Boolean>
    fun getFirebasePhoneAuthObservable(phoneNumberFieldValue: String): Observable<PhoneAuthEvent>?
    fun updateApproveStatusOfUser(phoneNumber: String, qrCode: String, updatedStatus: String): Observable<Boolean>
}