package com.fabsv.believers.believers.data.repository

import android.content.Context
import com.fabsv.believers.believers.data.source.local.UserLocalDataSource
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.data.source.remote.UserRemoteDataSource
import io.reactivex.Observable

class UserRepository(private val context: Context, val appPreferencesHelper: AppPreferencesHelper) {

    private var userLocalDataSource: UserLocalDataSource
    private var userRemoteDataSource: UserRemoteDataSource

    init {
        this.userLocalDataSource = UserLocalDataSource(context, appPreferencesHelper)
        this.userRemoteDataSource = UserRemoteDataSource(context, appPreferencesHelper)
    }

    fun loginWithPhoneNumber(phoneNumber: String): Observable<Boolean> {
        return userRemoteDataSource.loginWithPhoneNumber(phoneNumber)
    }

    fun onLogoutClicked(): Observable<Boolean> {
        return userLocalDataSource.onLogoutClicked()
    }
}