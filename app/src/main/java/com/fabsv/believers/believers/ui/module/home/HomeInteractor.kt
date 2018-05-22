package com.fabsv.believers.believers.ui.module.home

import android.content.Context
import com.fabsv.believers.believers.data.repository.UserRepository
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.fabsv.believers.believers.data.source.remote.model.AppData
import com.fabsv.believers.believers.data.source.remote.model.QuorumReportResponse
import io.reactivex.Observable

class HomeInteractor(context: Context, appPreferencesHelper: AppPreferencesHelper) {
    private var userRepository: UserRepository = UserRepository(context, appPreferencesHelper)

    fun getCollectionReport() = userRepository.getCollectionReport()
    fun getQuorumReport() :Observable<AppData<QuorumReportResponse>> = userRepository.getQuorumReport()

}
