package com.fabsv.believers.believers.data.source.remote

import android.content.Context
import com.fabsv.believers.believers.data.source.UserDataSource
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.data.source.remote.model.User
import com.fabsv.believers.believers.data.source.remote.retrofit.ApiClient
import com.fabsv.believers.believers.data.source.remote.retrofit.ApiInterface
import com.fabsv.believers.believers.util.methods.RxUtils
import io.reactivex.Observable

class UserRemoteDataSource(val context: Context, val appPreferencesHelper: AppPreferencesHelper) : UserDataSource {

    override fun loginWithPhoneNumber(phoneNumber: String): Observable<Boolean> {
        val apiInterface: ApiInterface = ApiClient.getClient()!!.create(ApiInterface::class.java)
        return apiInterface.userLogin(phoneNumber)
                .map { user: User -> "y".contentEquals(user.getStatus()!!) }
    }

    override fun onLogoutClicked(): Observable<Boolean> {
        return RxUtils.makeObservable(false)
    }
}