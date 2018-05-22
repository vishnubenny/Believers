package com.fabsv.believers.believers.ui.module.report

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.fabsv.believers.believers.R
import com.fabsv.believers.believers.data.source.remote.model.CollectionReportResponse
import com.fabsv.believers.believers.ui.base.MvpFragment
import com.fabsv.believers.believers.util.constants.AppConstants

class ReportFragment : MvpFragment<ReportContract.ReportView, ReportContract.ReportPresenter>(),
        ReportContract.ReportView {

    override fun onPrepareFragment(view: View?) {
        resetScreen()
    }

    override fun createPresenter(): ReportPresenter {
        return ReportPresenter(activity as Context, getAppPreferencesHelper())
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_report
    }

    private fun resetScreen() {
        updateToolbarTitle(activity?.getString(R.string.report), true)
    }

    companion object {
        fun getInstance(collectionReportResponse: CollectionReportResponse): Fragment {
            val fragment = ReportFragment()
            val bundle = Bundle()
            bundle.putSerializable(AppConstants.SerializableConstants.COLLECTION_REPORT, collectionReportResponse)
            fragment.arguments = bundle
            return fragment
        }
    }
}