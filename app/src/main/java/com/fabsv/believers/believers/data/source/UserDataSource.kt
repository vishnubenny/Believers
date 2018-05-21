package com.fabsv.believers.believers.data.source

import com.androidhuman.rxfirebase2.auth.PhoneAuthEvent
import com.fabsv.believers.believers.data.source.remote.model.*
import io.reactivex.Observable
import retrofit2.Response

interface UserDataSource {
    fun loginWithPhoneNumber(loginRequest: LoginRequest): Observable<Boolean>
    fun onLogoutClicked(): Observable<Boolean>
    fun getFirebasePhoneAuthObservable(phoneNumberFieldValue: String): Observable<PhoneAuthEvent>?
    fun requestQrCodeData(qrCode: String, mandalamId: String, meetingSlNo: String): Observable<Response<UserProfileResponse>>
    fun makeAttendancePresent(makeAttendancePresentModel: MakeAttendancePresentModel): Observable<Boolean>
    fun getCollectionReport(mandalamId: String, meetingSlNo: String, userId: String, mobile: String): Observable<AppData<CollectionReportResponse>>
}