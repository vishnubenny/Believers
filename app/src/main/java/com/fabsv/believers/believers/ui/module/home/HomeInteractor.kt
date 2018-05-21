package com.fabsv.believers.believers.ui.module.home

import android.content.Context
import com.fabsv.believers.believers.data.repository.UserRepository
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper

class HomeInteractor(context: Context, appPreferencesHelper: AppPreferencesHelper) {
    private var userRepository: UserRepository = UserRepository(context, appPreferencesHelper)

    fun getCollectionReport() = userRepository.getCollectionReport()

}
