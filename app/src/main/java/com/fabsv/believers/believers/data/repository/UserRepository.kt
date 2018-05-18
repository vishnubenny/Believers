package com.fabsv.believers.believers.data.repository

import android.content.Context
import android.util.StringBuilderPrinter
import com.androidhuman.rxfirebase2.auth.PhoneAuthEvent
import com.fabsv.believers.believers.data.source.local.UserLocalDataSource
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.data.source.remote.UserRemoteDataSource
import com.fabsv.believers.believers.data.source.remote.model.LoginRequest
import com.fabsv.believers.believers.data.source.remote.model.UserProfileResponse
import io.reactivex.Observable
import retrofit2.Response

class UserRepository(private val context: Context, val appPreferencesHelper: AppPreferencesHelper) {

    private var userLocalDataSource = UserLocalDataSource(context, appPreferencesHelper)
    private var userRemoteDataSource = UserRemoteDataSource(context, appPreferencesHelper)

    fun loginWithPhoneNumber(loginRequest: LoginRequest): Observable<Boolean> {
        return userRemoteDataSource.loginWithPhoneNumber(loginRequest)
    }

    fun onLogoutClicked(): Observable<Boolean> {
        return userLocalDataSource.onLogoutClicked()
    }

    fun getFirebasePhoneAuthObservable(phoneNumberFieldValue: String): Observable<PhoneAuthEvent>? {
        return userRemoteDataSource.getFirebasePhoneAuthObservable(phoneNumberFieldValue)
    }

    fun updateApproveStatusOfUser(phoneNumber: String, qrCode: String, updatedStatus: String): Observable<Boolean> {
        return userLocalDataSource.updateApproveStatusOfUser(phoneNumber, qrCode, updatedStatus)
    }

    fun requestQrCodeData(qrCode: String): Observable<Response<UserProfileResponse>>? {
        return userRemoteDataSource.requestQrCodeData(qrCode, appPreferencesHelper.getUserData().mandalamId.toString(), appPreferencesHelper.getUserData().meetingSlNo.toString())
    }
}