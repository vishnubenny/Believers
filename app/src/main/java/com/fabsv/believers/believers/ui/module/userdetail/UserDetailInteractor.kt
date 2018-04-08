package com.fabsv.believers.believers.ui.module.userdetail

import android.content.Context
import com.fabsv.believers.believers.data.repository.UserRepository
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import io.reactivex.Observable

class UserDetailInteractor(private val context: Context, private val appPreferencesHelper: AppPreferencesHelper) {
    private var userRepository: UserRepository

    fun updateApproveStatusOfUser(phoneNumber: String, qrCode: String, updatedStatus: String) : Observable<Boolean> {
        return this.userRepository.updateApproveStatusOfUser(phoneNumber, qrCode, updatedStatus)
    }

    init {
        this.userRepository = UserRepository(context, appPreferencesHelper)
    }
}