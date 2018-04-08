package com.fabsv.believers.believers.ui.module.report

import android.content.Context
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.lv.note.personalnote.ui.base.MvpBasePresenter

class ReportPresenter(val context: Context, val appPreferencesHelper: AppPreferencesHelper) :
        MvpBasePresenter<ReportContract.ReportView>(), ReportContract.ReportPresenter {

}