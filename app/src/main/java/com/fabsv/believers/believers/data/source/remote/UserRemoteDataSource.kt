package com.fabsv.believers.believers.data.source.remote

import android.app.Activity
import android.content.Context
import com.androidhuman.rxfirebase2.auth.PhoneAuthEvent
import com.androidhuman.rxfirebase2.auth.RxPhoneAuthProvider
import com.fabsv.believers.believers.data.source.UserDataSource
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.data.source.remote.model.LoginRequest
import com.fabsv.believers.believers.data.source.remote.model.LoginResponse
import com.fabsv.believers.believers.data.source.remote.model.MakeAttendancePresentModel
import com.fabsv.believers.believers.data.source.remote.model.UserProfileResponse
import com.fabsv.believers.believers.data.source.remote.retrofit.ApiInterface
import com.fabsv.believers.believers.data.source.remote.retrofit.ServiceGenerator
import com.fabsv.believers.believers.util.methods.RxUtils
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.Observable
import org.jetbrains.anko.AnkoLogger
import retrofit2.Response
import java.util.concurrent.TimeUnit

class UserRemoteDataSource(val context: Context, val appPreferencesHelper: AppPreferencesHelper) :
        UserDataSource, AnkoLogger {
    private var apiInterface: ApiInterface = ServiceGenerator.createService(ApiInterface::class.java)

    override fun loginWithPhoneNumber(loginRequest: LoginRequest): Observable<Boolean> {
        return this.apiInterface.userLogin(loginRequest.username, loginRequest.password, loginRequest.mobileNumber)
                .map { loginResponse: Response<LoginResponse> ->
                    if (200 == loginResponse.code()) {
                        loginResponse.body()?.let {
                            appPreferencesHelper.setUserData(it)
                        }
                    }
                    return@map 200 == loginResponse.code()
                }
    }

    override fun onLogoutClicked(): Observable<Boolean> {
        return RxUtils.makeObservable(false)
    }

    override fun getFirebasePhoneAuthObservable(phoneNumberFieldValue: String): Observable<PhoneAuthEvent>? {
        val phoneAuthProvider = PhoneAuthProvider.getInstance()
        return RxPhoneAuthProvider.verifyPhoneNumber(phoneAuthProvider, "+91$phoneNumberFieldValue", 120,
                TimeUnit.SECONDS, context as Activity)
    }

    override fun requestQrCodeData(qrCode: String, mandalamId: String, meetingSlNo: String): Observable<Response<UserProfileResponse>> {
        return apiInterface.getUserProfile(qrCode, mandalamId, meetingSlNo)
                .map { response: Response<UserProfileResponse> ->
                    response
                }
    }

    override fun makeAttendancePresent(makeAttendancePresentModel: MakeAttendancePresentModel) =
            apiInterface.makeAttendancePresent(makeAttendancePresentModel)
                    .map { response: Response<Any> -> 200 == response.code() }
}