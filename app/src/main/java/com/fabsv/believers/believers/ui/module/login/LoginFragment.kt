package com.fabsv.believers.believers.ui.module.login

import android.content.Context
import android.view.View
import com.fabsv.believers.believers.R
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

        resetScreen()
    }

    override fun createPresenter(): LoginPresenter {
        return LoginPresenter(activity as Context, getAppPreferencesHelper())
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_login
    }

    override fun getPhoneNumberField(): InitialValueObservable<CharSequence> {
        return RxTextView.textChanges(edit_text_phone_number)
    }

    override fun updateLoginButtonStatus(enable: Boolean) {
        button_login.isEnabled = enable
        if (enable) {
            utilityMethods.hideKeyboard(activity!!)
        }
    }

    override fun getLoginButtonClick(): Observable<Any> {
        return RxView.clicks(button_login)
    }

    override fun getPhoneNumberFieldValue(): String {
        return edit_text_phone_number.text.toString()
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
        edit_text_phone_number.setText("")
        presetLoggedInUserPhoneNumber()
        presenter!!.validate()
    }

    override fun onLoginFailure() {
        showShortToast(getString(R.string.login_failed_contact_your_admin))
    }

    private fun presetLoggedInUserPhoneNumber() {
        val loggedInUserPhoneNumber: String = getAppPreferencesHelper().getLoggedInUserPhoneNumber()
        if (loggedInUserPhoneNumber.isNotEmpty()) {
            edit_text_phone_number.setText(loggedInUserPhoneNumber)
            edit_text_phone_number.setSelection(edit_text_phone_number.length())
        }
    }

    companion object {
        fun getInstance(): LoginFragment {
            val loginFragment = LoginFragment()
            return loginFragment
        }
    }
}