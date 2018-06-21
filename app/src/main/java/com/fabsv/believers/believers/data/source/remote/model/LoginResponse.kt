package com.fabsv.believers.believers.data.source.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponse {
    @SerializedName("UserId")
    @Expose
    var userId: Int? = null

    @SerializedName("MandalamId")
    @Expose
    var mandalamId: Int? = null

    @SerializedName("MeetingSlno")
    @Expose
    var meetingSlNo: Int? = null

    @SerializedName("UserName")
    @Expose
    var userName: String? = null
}
