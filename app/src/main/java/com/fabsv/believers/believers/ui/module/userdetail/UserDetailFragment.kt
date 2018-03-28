package com.fabsv.believers.believers.ui.module.userdetail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.app.Fragment
import android.util.Base64
import android.view.View
import com.fabsv.believers.believers.R
import com.fabsv.believers.believers.ui.base.MvpFragment
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_user_detail.*
import java.io.ByteArrayOutputStream

class UserDetailFragment : MvpFragment<UserDetailContract.UserDetailView, UserDetailContract.UserDetailPresenter>(),
        UserDetailContract.UserDetailView {
    override fun onPrepareFragment(view: View?) {
        presenter!!.validate()

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

    override fun exitUserDetailScreen() {
        popBackCurrentFragment()
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

    companion object {
        fun getInstance(): Fragment {
            val fragment = UserDetailFragment()
            return fragment
        }
    }
}