package com.fabsv.believers.believers.ui.module.report

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.fabsv.believers.believers.R
import com.fabsv.believers.believers.data.source.remote.model.CollectionReportResponse
import com.fabsv.believers.believers.data.source.remote.model.QuorumReportResponse
import com.fabsv.believers.believers.databinding.FragmentReportBinding
import com.fabsv.believers.believers.ui.base.MvpFragment
import com.fabsv.believers.believers.util.constants.AppConstants
import kotlinx.android.synthetic.main.fragment_report.*

class ReportFragment : MvpFragment<ReportContract.ReportView, ReportContract.ReportPresenter>(),
        ReportContract.ReportView {
    private var collectionReportResponse: CollectionReportResponse? = null
    private var quorumReportResponse: QuorumReportResponse? = null

    override fun onPrepareFragment(view: View?) {
        presetScreen(view)
        resetScreen()
    }

    override fun createPresenter(): ReportPresenter {
        return ReportPresenter(activity as Context, getAppPreferencesHelper())
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_report
    }

    private fun presetScreen(view: View?) {
        presetToolbarAndParseReportData()

        view?.let {
            val fragmentReportBinding: FragmentReportBinding? = DataBindingUtil.bind(it)
            fragmentReportBinding?.let {
                collectionReportResponse?.let { report: CollectionReportResponse ->
                    it.collectionReport = report
                    layout_quorum_report.visibility = View.GONE
                }

                quorumReportResponse?.let { report: QuorumReportResponse ->
                    it.quorumReport = report
                    layout_collection_report.visibility = View.GONE
                }
                it.executePendingBindings()
            }
        }
    }

    private fun resetScreen() {

    }

    private fun presetToolbarAndParseReportData() {
        arguments?.let {
            if (it.containsKey(AppConstants.SerializableConstants.COLLECTION_REPORT)) {
                updateToolbarTitle(activity?.getString(R.string.collection_report), true)
                collectionReportResponse = it.getSerializable(AppConstants.SerializableConstants.COLLECTION_REPORT)
                        as CollectionReportResponse
            }

            if (it.containsKey(AppConstants.SerializableConstants.QUORUM_REPORT)) {
                updateToolbarTitle(activity?.getString(R.string.quorum_report), true)
                quorumReportResponse = it.getSerializable(AppConstants.SerializableConstants.QUORUM_REPORT)
                        as QuorumReportResponse
            }
        }
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