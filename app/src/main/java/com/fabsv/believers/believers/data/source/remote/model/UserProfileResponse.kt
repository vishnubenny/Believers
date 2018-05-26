package com.fabsv.believers.believers.data.source.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserProfileResponse : Serializable {
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

    @SerializedName("MemberName")
    @Expose
    var memberName: String? = null

    @SerializedName("MemberCode")
    @Expose
    var memberCode: String? = null

    @SerializedName("MemberQRCode")
    @Expose
    var memberQrCode: String? = null

    @SerializedName("MemberAddress")
    @Expose
    var memberAddress: String? = null

    @SerializedName("Photo")
    @Expose
    var photo: String? = null

    @SerializedName("MemberParish")
    @Expose
    var memberParish: String? = null

    @SerializedName("MemberParishVicar")
    @Expose
    var memberParishVicar: String? = null

    @SerializedName("MemberDiocese")
    @Expose
    var memberDiocese: String? = null

    @SerializedName("AttType")
    @Expose
    var attType: String? = null

    @SerializedName("RegFee")
    @Expose
    var regFee: Int? = null
}

/*
class UserProfileResponse {
    @SerializedName("MemberName")
    @Expose
    var memberName: String? = null
}*/
