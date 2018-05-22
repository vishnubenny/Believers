package com.fabsv.believers.believers.data.source.remote.model

import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MakeAttendancePresentModel {
    @SerializedName("MandalamId")
    @Expose
    var mandalamId: Int? = null

    @SerializedName("MeetingSlno")
    @Expose
    var meetingSlNo: Int? = null

    @SerializedName("AttSlno")
    @Expose
    var attSlNo: Int? = null

    @SerializedName("MemberSlno")
    @Expose
    var memberSlNo: Int? = null

    @SerializedName("LoginUser")
    @Expose
    var loginUser: Int? = null

    @SerializedName("LoginIp")
    @Expose
    var loginIp: String? = null

    companion object {
        fun create(userProfileResponse: UserProfileResponse, appPreferencesHelper: AppPreferencesHelper, ip: String):
                MakeAttendancePresentModel {
            val makeAttendancePresentModel = MakeAttendancePresentModel()
            makeAttendancePresentModel.mandalamId = userProfileResponse.mandalamId
            makeAttendancePresentModel.meetingSlNo = userProfileResponse.meetingSlNo
            makeAttendancePresentModel.attSlNo = userProfileResponse.attSlNo
            makeAttendancePresentModel.memberSlNo = userProfileResponse.memberSlNo
            makeAttendancePresentModel.loginUser = appPreferencesHelper.getUserData().userId
            makeAttendancePresentModel.loginIp = ip
            return makeAttendancePresentModel
        }
    }
}