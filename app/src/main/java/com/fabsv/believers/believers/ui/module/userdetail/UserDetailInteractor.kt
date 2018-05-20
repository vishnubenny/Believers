package com.fabsv.believers.believers.ui.module.userdetail

import android.content.Context
import com.fabsv.believers.believers.data.repository.UserRepository
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.data.source.remote.model.MakeAttendancePresentModel
import io.reactivex.Observable

class UserDetailInteractor(private val context: Context, private val appPreferencesHelper: AppPreferencesHelper) {
    private var userRepository: UserRepository

    fun makeAttendancePresent(makeAttendancePresentModel: MakeAttendancePresentModel) =
            userRepository.makeAttendancePresent(makeAttendancePresentModel)

    init {
        this.userRepository = UserRepository(context, appPreferencesHelper)
    }
}