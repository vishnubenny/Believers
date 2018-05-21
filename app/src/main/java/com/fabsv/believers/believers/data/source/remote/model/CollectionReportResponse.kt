package com.fabsv.believers.believers.data.source.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CollectionReportResponse {
    @SerializedName("MemberCount")
    @Expose
    var memberCount: Int? = null

    @SerializedName("RegFeeAmount")
    @Expose
    var regFeeAmount: Int? = null
}