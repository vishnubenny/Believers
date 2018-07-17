package com.fabsv.believers.believers.ui.module.scan

import android.content.Context
import com.fabsv.believers.believers.data.repository.UserRepository
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.data.source.remote.model.UserProfileResponse
import io.reactivex.Observable
import retrofit2.Response

class ScanInteractor(val context: Context, val appPreferencesHelper: AppPreferencesHelper) {
    private var userRepository: UserRepository = UserRepository(context, appPreferencesHelper)

    fun requestQrCodeData(qrCode: String): Observable<Response<UserProfileResponse>>? {
        return userRepository.requestQrCodeData(qrCode)
    }

    fun requestQrCodeDataFromSearch(qrCode: String): Observable<Response<UserProfileResponse>>? =
            userRepository.requestQrCodeDataFromSearch(qrCode)
}