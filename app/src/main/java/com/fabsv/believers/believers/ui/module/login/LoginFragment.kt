package com.fabsv.believers.believers.ui.module.login

import android.content.Context
import android.view.View
import com.fabsv.believers.believers.R
import com.fabsv.believers.believers.data.source.remote.model.LoginRequest
import com.fabsv.believers.believers.ui.base.MvpFragment
import com.fabsv.believers.believers.util.methods.utilityMethods
import com.jakewharton.rxbinding2.InitialValueObservable
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : MvpFragment<LoginContract.LoginView, LoginContract.LoginPresenter>(),
        LoginContract.LoginView {
    override fun onPrepareFragment(view: View?) {

        presetScreen()
        resetScreen()
    }

    override fun createPresenter(): LoginPresenter {
        return LoginPresenter(activity as Context, getAppPreferencesHelper())
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_login
    }

    override fun getUsernameField(): InitialValueObservable<CharSequence> {
        return RxTextView.textChanges(edit_text_username)
    }

    override fun getPasswordField(): InitialValueObservable<CharSequence> {
        return RxTextView.textChanges(edit_text_password)
    }

    override fun getPhoneNumberField(): InitialValueObservable<CharSequence> {
        return RxTextView.textChanges(edit_text_phone_number)
    }

    override fun getOtpNumberObservable(): InitialValueObservable<CharSequence> {
        return RxTextView.textChanges(edit_text_otp)
    }

    override fun updateLoginButtonStatus(enable: Boolean) {
        button_login.isEnabled = enable
    }

    override fun updateVerifyOtpButtonStatus(isValidOtp: Boolean) {
        button_verify_otp.isEnabled = isValidOtp
        if (isValidOtp) {
            utilityMethods.hideKeyboard(activity!!)
        }
    }

    /**
     * Updates verify/login layout visibility
     */
    override fun updateVerifyAuthCodeLayoutStatus(showVerifyLayout: Boolean) {
        if (showVerifyLayout) {
            //hide layout login and layout retry attempt
            layout_login.visibility = View.GONE
            layout_retry_attempt.visibility = View.GONE
            //make visible layout verify
            layout_verify.visibility = View.VISIBLE
        } else {
            //hide layout
            layout_verify.visibility = View.GONE
            layout_retry_attempt.visibility = View.GONE
            //make visible layout login
            layout_login.visibility = View.VISIBLE
            //phone number edit text set default
            setFieldsDefault()
        }
    }

    override fun updateOtpAttemptFailRetryLayoutStatus(isShowOtpRetryLayout: Boolean) {
        if (isShowOtpRetryLayout) {
            //hide login and verify otp layout
            layout_login.visibility = View.GONE
            layout_verify.visibility = View.GONE
            //show otp auth attempt fail: retry layout
            layout_retry_attempt.visibility = View.VISIBLE
        }
    }

    override fun getLoginButtonClick(): Observable<Any> {
        return RxView.clicks(button_login)
    }

    override fun getVerifyOtpButtonClick(): Observable<Any> {
        return RxView.clicks(button_verify_otp)
    }

    override fun getRetryButtonClick(): Observable<Any> {
        return RxView.clicks(button_retry)
    }

    override fun getChangeNumberButtonClick(): Observable<Any> {
        return RxView.clicks(button_change_number)
    }

    override fun getLoginRequestModel() = LoginRequest(getUsername(), getPassword(), getMobile())

    override fun getVerifyOtpFieldValue(): String {
        return edit_text_otp.text.toString()
    }

    override fun onLoginSuccess() {
        presenter!!.showHomeFragment()
    }

    /**
     * UnSubscribe to the existing validations if any
     * Resetting the edit text field value
     * Subscribing to the validations
     */
    override fun resetScreen() {
        presenter!!.unSubscribeValidations()
        updateVerifyAuthCodeLayoutStatus(false)
        presetLoggedInUserLoginDetails()
        presenter!!.validate()
    }

    override fun onLoginFailure() {
        showShortToast(getString(R.string.something_went_wrong_please_contact_admin))
    }

    private fun presetLoggedInUserLoginDetails() {
        val loggedInUserUsername = getAppPreferencesHelper().getLoggedInUserUsername()
        loggedInUserUsername?.let {
            if (it.isNotEmpty()) {
                edit_text_username.setText(it)

                /*
                If user already logged in successfully, then the username and password will preset in fields
                In that case we have to request the focus in the password field
                */
                edit_text_password.requestFocus()
            }
        }

        val loggedInUserPhoneNumber: String = getAppPreferencesHelper().getLoggedInUserPhoneNumber()
        if (loggedInUserPhoneNumber.isNotEmpty())
            edit_text_phone_number.setText(loggedInUserPhoneNumber)
    }

    private fun setFieldsDefault() {
        edit_text_username.setText("")
        edit_text_password.setText("")
        edit_text_phone_number.setText("")
        edit_text_username.requestFocus()
    }

    private fun presetScreen() {
        updateToolbarTitle(activity?.resources?.getString(R.string.login), false)
    }

    private fun getUsername() = edit_text_username.text.toString()

    private fun getPassword() = edit_text_password.text.toString()

    private fun getMobile() = edit_text_phone_number.text.toString()

    companion object {
        fun getInstance(): LoginFragment {
            val loginFragment = LoginFragment()
            return loginFragment
        }
    }
}