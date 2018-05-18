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
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_user_detail.*
import java.io.ByteArrayOutputStream

class UserDetailFragment : MvpFragment<UserDetailContract.UserDetailView, UserDetailContract.UserDetailPresenter>(),
        UserDetailContract.UserDetailView {
    override fun onPrepareFragment(view: View?) {
        resetScreen()

        testBase64Image()
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

    override fun getScannedQrCode(): String {
        return ""
    }

    override fun exitUserDetailScreen() {
        popBackCurrentFragment()
    }

    override fun onApproveStatusUpdateSuccess() {
        exitUserDetailScreen()
    }

    override fun onApproveStatusUpdateFailed() {
        showShortToast(getString(R.string.something_went_wrong_please_contact_admin))
    }

    private fun resetScreen() {
        presenter?.unSubscribeValidations()
        presetScreen()
        presenter?.validate()
    }

    private fun presetUserProfile() {
        val bundle: Bundle? = arguments
        bundle?.let {
            val userProfileResponse = it.getSerializable(AppConstants.SerializableConstants.USER_PROFILE) as UserProfileResponse
            userProfileResponse.memberName?.let { it1 -> text_view_user_name.setText(it1) }
            userProfileResponse.memberAddress?.let { it1 -> text_view_user_address.setText(it1) }
            userProfileResponse.memberCode?.let { it1 -> text_view_user_member_code.setText(it1) }
        }
    }

    private fun testBase64Image() {
        val baos = ByteArrayOutputStream()
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.thumb1)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        var imageBytes = baos.toByteArray()
        val imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT)

        imageBytes = Base64.decode(imageString, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        image_user_pic.setImageBitmap(decodedImage)
    }

    private fun presetScreen() {
        presetUserProfile()
        updateToolbarTitle(activity?.getString(R.string.user_profile), homeUpEnabled = true)
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