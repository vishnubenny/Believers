package com.fabsv.believers.believers.data.source.remote

import android.app.Activity
import android.content.Context
import com.androidhuman.rxfirebase2.auth.PhoneAuthEvent
import com.androidhuman.rxfirebase2.auth.RxPhoneAuthProvider
import com.fabsv.believers.believers.data.source.UserDataSource
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.data.source.remote.model.LoginResponse
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
    private var apiInterface: ApiInterface

    override fun loginWithPhoneNumber(phoneNumber: String): Observable<Boolean> {
        return this.apiInterface.userLogin(phoneNumber)
                .map { loginResponse: Response<LoginResponse> ->
                    val userId = loginResponse.body()?.userId
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

    override fun updateApproveStatusOfUser(phoneNumber: String, qrCode: String, updatedStatus: String): Observable<Boolean> {
        return this.apiInterface.updateApproveStatusOfUser(phoneNumber, qrCode, updatedStatus)
                .map { loginResponse: LoginResponse -> 200 == loginResponse.userId }
    }

    init {
        /*this.apiInterface = ApiClient.getClient()!!.create(ApiInterface::class.java)*/
        this.apiInterface = ServiceGenerator.createService(ApiInterface::class.java)
    }
}