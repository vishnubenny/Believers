package com.fabsv.believers.believers.ui.module.home

import android.content.Context
import android.view.View
import com.fabsv.believers.believers.R
import com.fabsv.believers.believers.ui.base.MvpFragment
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : MvpFragment<HomeContract.HomeView, HomeContract.HomePresenter>(), HomeContract.HomeView {
    private var collectionReportButtonClickEvent = PublishSubject.create<Boolean>()
    private var quorumReportButtonClickEvent = PublishSubject.create<Boolean>()

    override fun onPrepareFragment(view: View?) {

        presetScreen()
        resetScreen()
    }

    override fun createPresenter(): HomePresenter {
        return HomePresenter(activity as Context, getAppPreferencesHelper())
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home
    }

    override fun resetScreen() {
        presenter?.unSubscribeValidations()
        presenter?.validate()
    }

    override fun getLogoutButtonClickEvent(): Observable<Any> {
        return RxView.clicks(button_logout)
    }

    override fun getScanButtonClickEvent(): Observable<Any> {
        return RxView.clicks(button_scan)
    }

    override fun getCollectionReportButtonClickEvent(): Observable<Boolean> {
        return collectionReportButtonClickEvent
    }

    override fun getQuorumReportButtonClickEvent(): Observable<Boolean> {
        return quorumReportButtonClickEvent
    }

    override fun showLoggedInUserPhoneNumber(phoneNumber: String) {
        text_view_logged_in_user_phone_number.text = phoneNumber
    }

    private fun presetScreen() {
        presenter?.showLoggedInUserDetail()
        updateToolbarTitle(activity?.resources?.getString(R.string.home), homeUpEnabled = false)
        button_collective_report.setOnClickListener {
            collectionReportButtonClickEvent.onNext(true)
        }
        button_quorum_report.setOnClickListener {
            quorumReportButtonClickEvent.onNext(true)
        }
    }

    companion object {
        fun getInstance(): HomeFragment {
            val homeFragment = HomeFragment()
            return homeFragment
        }
    }
}