package com.fabsv.believers.believers.ui.module.home

import android.content.Context
import android.view.View
import com.fabsv.believers.believers.R
import com.fabsv.believers.believers.ui.base.MvpFragment
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : MvpFragment<HomeContract.HomeView, HomeContract.HomePresenter>(), HomeContract.HomeView {
    override fun onPrepareFragment(view: View?) {

        resetScreen()
    }

    override fun createPresenter(): HomePresenter {
        return HomePresenter(activity as Context, getAppPreferencesHelper())
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home
    }

    private fun resetScreen() {
        presenter?.unSubscribeValidations()
        presenter?.showLoggedInUserDetail()
        presetScreen()
        presenter?.validate()
    }

    override fun getLogoutButtonClickEvent(): Observable<Any> {
        return RxView.clicks(button_logout)
    }

    override fun getScanButtonClickEvent(): Observable<Any> {
        return RxView.clicks(button_scan)
    }

    override fun getCollectionReportButtonClickEvent(): Observable<Any> {
        return RxView.clicks(button_collective_report)
    }

    override fun getQuorumReportButtonClickEvent(): Observable<Any> {
        return RxView.clicks(button_quorum_report)
    }

    override fun showLoggedInUserPhoneNumber(phoneNumber: String) {
        text_view_logged_in_user_phone_number.text = phoneNumber
    }

    private fun presetScreen() {
        updateToolbarTitle(activity?.resources?.getString(R.string.home), homeUpEnabled = false)
    }

    companion object {
        fun getInstance(): HomeFragment {
            val homeFragment = HomeFragment()
            return homeFragment
        }
    }
}