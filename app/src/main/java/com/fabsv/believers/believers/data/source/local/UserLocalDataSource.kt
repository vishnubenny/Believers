package com.fabsv.believers.believers.data.source.local

import android.content.Context
import com.androidhuman.rxfirebase2.auth.PhoneAuthEvent
import com.fabsv.believers.believers.data.source.UserDataSource
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.data.source.remote.model.*
import com.fabsv.believers.believers.util.constants.AppConstants
import com.fabsv.believers.believers.util.methods.RxUtils
import io.reactivex.Observable
import retrofit2.Response

class UserLocalDataSource(val context: Context, val appPreferencesHelper: AppPreferencesHelper) : UserDataSource {
    override fun loginWithPhoneNumber(loginRequest: LoginRequest): Observable<Boolean> {
        val status = (AppConstants.LoginConstants.DUMMY_PHONE_NUMBER).equals(loginRequest.mobileNumber) ||
                (AppConstants.LoginConstants.DUMMY_PHONE_NUMBER_1).equals(loginRequest.mobileNumber) ||
                (AppConstants.LoginConstants.DUMMY_PHONE_NUMBER_2).equals(loginRequest.mobileNumber)
        return Observable.just(status)
    }

    override fun onLogoutClicked(): Observable<Boolean> {
        appPreferencesHelper.setLoggedIn(false)
        return RxUtils.makeObservable(true)
    }

    override fun getFirebasePhoneAuthObservable(phoneNumberFieldValue: String): Observable<PhoneAuthEvent>? {
        return null
    }

    override fun requestQrCodeData(qrCode: String, mandalamId: String, meetingSlNo: String): Observable<Response<UserProfileResponse>> = Observable.just(Response.success(UserProfileResponse()))

    override fun makeAttendancePresent(makeAttendancePresentModel: MakeAttendancePresentModel) =
            Observable.just(false)

    override fun getCollectionReport(mandalamId: String, meetingSlNo: String, userId: String, mobile: String):
            Observable<AppData<CollectionReportResponse>> {
        val appData = AppData<CollectionReportResponse>()
        val collectionReportResponse = CollectionReportResponse()
        collectionReportResponse.memberCount = 10
        collectionReportResponse.regFeeAmount = 6000
        appData.data = collectionReportResponse
        return RxUtils.makeObservable(appData)
    }

    override fun getQuorumReport(mandalamId: String, meetingSlNo: String, dateTimeObject: String): Observable<AppData<QuorumReportResponse>> {
        return RxUtils.makeObservable(AppData())
    }
}