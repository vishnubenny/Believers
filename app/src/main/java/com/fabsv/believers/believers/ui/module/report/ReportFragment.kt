package com.fabsv.believers.believers.ui.module.report

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.fabsv.believers.believers.R
import com.fabsv.believers.believers.data.source.remote.model.CollectionReportResponse
import com.fabsv.believers.believers.data.source.remote.model.QuorumReportResponse
import com.fabsv.believers.believers.ui.base.MvpFragment
import com.fabsv.believers.believers.util.constants.AppConstants

class ReportFragment : MvpFragment<ReportContract.ReportView, ReportContract.ReportPresenter>(),
        ReportContract.ReportView {

    override fun onPrepareFragment(view: View?) {
        presetScreen()
        resetScreen()
    }

    override fun createPresenter(): ReportPresenter {
        return ReportPresenter(activity as Context, getAppPreferencesHelper())
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_report
    }

    private fun presetScreen(){
        arguments?.containsKey(AppConstants.SerializableConstants.COLLECTION_REPORT)?.let {
            if (it) {
                updateToolbarTitle(activity?.getString(R.string.collection_report), true)
            }
        }

        arguments?.containsKey(AppConstants.SerializableConstants.QUORUM_REPORT)?.let {
            if (it) {
                updateToolbarTitle(activity?.getString(R.string.quorum_report), true)
            }
        }
    }

    private fun resetScreen() {

    }

    companion object {
        fun getInstance(reportResponse: Any): Fragment {
            val fragment = ReportFragment()
            val bundle = Bundle()
            if (reportResponse is CollectionReportResponse) {
                bundle.putSerializable(AppConstants.SerializableConstants.COLLECTION_REPORT, reportResponse)
            } else if (reportResponse is QuorumReportResponse) {
                bundle.putSerializable(AppConstants.SerializableConstants.QUORUM_REPORT, reportResponse)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}