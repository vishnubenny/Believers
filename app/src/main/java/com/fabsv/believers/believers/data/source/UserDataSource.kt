package com.fabsv.believers.believers.data.source

import com.androidhuman.rxfirebase2.auth.PhoneAuthEvent
import com.fabsv.believers.believers.data.source.remote.model.LoginRequest
import com.fabsv.believers.believers.data.source.remote.model.UserProfileResponse
import io.reactivex.Observable
import retrofit2.Response

interface UserDataSource {
    fun loginWithPhoneNumber(loginRequest: LoginRequest): Observable<Boolean>
    fun onLogoutClicked(): Observable<Boolean>
    fun getFirebasePhoneAuthObservable(phoneNumberFieldValue: String): Observable<PhoneAuthEvent>?
    fun updateApproveStatusOfUser(phoneNumber: String, qrCode: String, updatedStatus: String): Observable<Boolean>
    fun requestQrCodeData(qrCode: String, mandalamId: String, meetingSlNo: String): Observable<Response<UserProfileResponse>>
}