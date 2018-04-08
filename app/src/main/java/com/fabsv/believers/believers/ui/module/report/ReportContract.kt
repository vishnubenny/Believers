package com.fabsv.believers.believers.ui.module.report

import com.fabsv.believers.believers.ui.base.MvpView
import com.lv.note.personalnote.ui.base.MvpPresenter

interface ReportContract {

    interface ReportView : MvpView {

    }

    interface ReportPresenter : MvpPresenter<ReportContract.ReportView> {

    }
}