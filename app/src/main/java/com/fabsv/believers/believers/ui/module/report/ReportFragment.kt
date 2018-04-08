package com.fabsv.believers.believers.ui.module.report

import android.content.Context
import android.support.v4.app.Fragment
import android.view.View
import com.fabsv.believers.believers.R
import com.fabsv.believers.believers.ui.base.MvpFragment

class ReportFragment : MvpFragment<ReportContract.ReportView, ReportContract.ReportPresenter>(),
        ReportContract.ReportView {

    override fun onPrepareFragment(view: View?) {

    }

    override fun createPresenter(): ReportPresenter {
        return ReportPresenter(activity as Context, getAppPreferencesHelper())
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_report
    }

    companion object {
        fun getInstance(): Fragment {
            val fragment = ReportFragment()
            return fragment
        }
    }
}