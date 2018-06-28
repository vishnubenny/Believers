package com.fabsv.believers.believers.ui.module.userdetail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Base64
import android.view.View
import com.fabsv.believers.believers.R
import com.fabsv.believers.believers.data.source.remote.model.UserProfileResponse
import com.fabsv.believers.believers.ui.base.MvpFragment
import com.fabsv.believers.believers.util.constants.AppConstants
import com.fabsv.believers.believers.util.methods.RxUtils
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_user_detail.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.ByteArrayOutputStream

class UserDetailFragment : MvpFragment<UserDetailContract.UserDetailView, UserDetailContract.UserDetailPresenter>(),
        UserDetailContract.UserDetailView, AnkoLogger {
    private var userProfileResponse: UserProfileResponse? = null

    override fun onPrepareFragment(view: View?) {
        presetScreen()
        resetScreen()
    }

    override fun createPresenter(): UserDetailPresenter {
        return UserDetailPresenter(activity as Context, getAppPreferencesHelper())
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_user_detail
    }

    override fun getRejectButtonClickEvent(): Observable<Any> {
        return RxView.clicks(button_reject)
    }

    override fun getApproveButtonClickEvent(): Observable<Any> {
        return RxView.clicks(button_approve)
    }

    override fun getUserProfile() = userProfileResponse

    override fun getUserProfileValidity(): Observable<Boolean> {
        val usernameValidity = (null == userProfileResponse || null == userProfileResponse?.memberName)
        var usernameEmptyValidity = false
        var alreadyPresented = false
        userProfileResponse?.let {
            it.memberName?.let {
                usernameEmptyValidity = it.isEmpty()
            }
            it.attType?.let {
                alreadyPresented = it.equals("P", true)
            }
        }
        val usernameBasedValidity = usernameValidity || usernameEmptyValidity
        return RxUtils.makeObservable(usernameBasedValidity || alreadyPresented)
    }

    override fun exitUserDetailScreen() {
        popBackCurrentFragment()
    }

    override fun updateApproveButtonEnableStatus(enable: Boolean) {
        button_approve.isEnabled = enable
    }

    override fun onApproveStatusUpdateSuccess() {
        showSuccessShortToast(getString(R.string.attendance_updated_successfully))
        exitUserDetailScreen()
    }

    override fun onApproveStatusUpdateFailed() {
        showFailedShortToast(getString(R.string.attendance_update_failed))
    }

    override fun resetScreen() {
        presenter?.unSubscribeValidations()
        presenter?.validate()
    }

    private fun presetUserProfile() {
        val bundle: Bundle? = arguments
        bundle?.let {
            userProfileResponse = it.getSerializable(AppConstants.SerializableConstants.USER_PROFILE) as UserProfileResponse
            userProfileResponse?.let {
                it.photo?.let {
                    loadBase64Image(it)
                }
                if (null == it.memberName) {
                    hideUserDetailsPane()
                }
                it.memberCode?.let {
                    text_view_user_reg_no.setText(it)
                }
                it.memberName?.let {
                    if (it.isEmpty()) {
                        hideUserDetailsPane()
                    } else {
                        text_view_user_name.setText(it)
                    }
                }
                it.memberAddress?.let {
                    text_view_user_address.setText(it)
                }
                it.memberParish?.let {
                    text_view_user_parish.setText(it)
                }
                it.memberDiocese?.let {
                    text_view_user_diocese.setText(it)
                }
                it.regFee?.let {
                    text_view_user_reg_fee.text = it.toString()
                }
                it.attType?.let {
                    if (it.equals("P", true)) {
                        text_view_profile_present_info.visibility = View.VISIBLE
                    } else {
                        text_view_profile_present_info.visibility = View.GONE
                    }
                }
            }

            if (null == userProfileResponse) {
                hideUserDetailsPane()
            }
        }
    }

    private fun hideUserDetailsPane() {
        text_view_no_user_existing.visibility = View.VISIBLE
        scroll_view_user_detail_pane.visibility = View.GONE
    }

    private fun loadDefaultBase64Image() {
        val baos = ByteArrayOutputStream()
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.image_not_available)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes = baos.toByteArray()
        val imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT)

        loadBase64Image(imageString)
    }

    private fun loadBase64Image(imageString: String?) {
        imageString?.let {
            try {
                val imageBytes = Base64.decode(it, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                image_user_pic.setImageBitmap(Bitmap.createScaledBitmap(decodedImage, 150,
                        150, false))
            } catch (e: Exception) {
                info(e)
                loadDefaultBase64Image()
            }
        }
    }

    private fun presetScreen() {
        updateToolbarTitle(activity?.getString(R.string.user_profile), homeUpEnabled = true)
        presetUserProfile()
    }

    companion object {
        fun getInstance(userProfileResponse: UserProfileResponse): Fragment {
            val fragment = UserDetailFragment()
            val bundle = Bundle()
            bundle.putSerializable(AppConstants.SerializableConstants.USER_PROFILE, userProfileResponse)
            fragment.arguments = bundle
            return fragment
        }
    }
}