package com.fabsv.believers.believers.data.source.remote.model

import android.util.ArrayMap
import com.fabsv.believers.believers.data.source.local.prefs.AppPreferencesHelper
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import okhttp3.RequestBody
import org.json.JSONObject

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

    @SerializedName("RegFee")
    @Expose
    var regFee: Int? = null

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
            makeAttendancePresentModel.regFee = userProfileResponse.regFee
            return makeAttendancePresentModel
        }

        fun createRequestBody(makeAttendancePresentModel: MakeAttendancePresentModel): RequestBody {
            val jsonParams = ArrayMap<String, Any>()
            jsonParams.put("MandalamId", makeAttendancePresentModel.mandalamId)
            jsonParams.put("MeetingSlno", makeAttendancePresentModel.meetingSlNo)
            jsonParams.put("AttSlno", makeAttendancePresentModel.attSlNo)
            jsonParams.put("MemberSlno", makeAttendancePresentModel.memberSlNo)
            jsonParams.put("LoginUser", makeAttendancePresentModel.loginUser)
            jsonParams.put("RegFee", makeAttendancePresentModel.regFee)
            jsonParams.put("LoginIp", makeAttendancePresentModel.loginIp)

            return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                    JSONObject(jsonParams).toString())
        }
    }
}