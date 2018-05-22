package com.fabsv.believers.believers.data.repository

import android.content.Context
import com.androidhuman.rxfirebase2.auth.PhoneAuthEvent
import com.fabsv.believers.believers.data.source.local.UserLocalDataSource
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.data.source.remote.UserRemoteDataSource
import com.fabsv.believers.believers.data.source.remote.model.*
import com.fabsv.believers.believers.ui.utils.AppDateUtils
import com.fabsv.believers.believers.util.methods.RxUtils
import io.reactivex.Observable
import retrofit2.Response
import java.util.*

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

    fun requestQrCodeData(qrCode: String): Observable<Response<UserProfileResponse>>? {
        return userRemoteDataSource.requestQrCodeData(qrCode, appPreferencesHelper.getUserData().mandalamId.toString(), appPreferencesHelper.getUserData().meetingSlNo.toString())
    }

    fun makeAttendancePresent(makeAttendancePresentModel: MakeAttendancePresentModel) =
            userRemoteDataSource.makeAttendancePresent(makeAttendancePresentModel)

    fun getCollectionReport(): Observable<AppData<CollectionReportResponse>> {
        val mandalamId = appPreferencesHelper.getUserData().mandalamId?.toString()
        val meetingSlNo = appPreferencesHelper.getUserData().meetingSlNo?.toString()
        val userId = appPreferencesHelper.getUserData().userId?.toString()
        val mobileNo = appPreferencesHelper.getLoggedInUserPhoneNumber()

        if (null != mandalamId && null != meetingSlNo && null != userId) {
            return userRemoteDataSource.getCollectionReport(mandalamId, meetingSlNo, userId, mobileNo)
        }
        return Observable.just(AppData())
    }

    fun getQuorumReport(): Observable<AppData<QuorumReportResponse>> {
        val mandalamId = appPreferencesHelper.getUserData().mandalamId?.toString()
        val meetingSlNo = appPreferencesHelper.getUserData().meetingSlNo?.toString()
        if (null != mandalamId && null != meetingSlNo) {
            return userRemoteDataSource.getQuorumReport(mandalamId, meetingSlNo, AppDateUtils.getDateTimeObject(Date()))
        }
        return RxUtils.makeObservable(AppData())
    }
}