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

        presenter!!.validate()
        presenter!!.showLoggedInUserDetail()
    }

    override fun createPresenter(): HomePresenter {
        return HomePresenter(activity as Context, getAppPreferencesHelper())
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home
    }

    override fun getLogoutButtonClickEvent(): Observable<Any> {
        return RxView.clicks(button_logout)
    }

    override fun getScanButtonClickEvent(): Observable<Any> {
        return RxView.clicks(button_scan)
    }

    override fun getReportButtonClickEvent(): Observable<Any> {
        return RxView.clicks(button_report)
    }

    override fun showLoggedInUserPhoneNumber(phoneNumber: String) {
        text_view_logged_in_user_phone_number.text = phoneNumber
    }

    companion object {
        fun getInstance(): HomeFragment {
            val homeFragment = HomeFragment()
            return homeFragment
        }
    }
}