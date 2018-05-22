package com.fabsv.believers.believers.data.source.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CollectionReportResponse : Serializable {
    @SerializedName("MemberCount")
    @Expose
    var memberCount: Int? = null

    @SerializedName("RegFeeAmount")
    @Expose
    var regFeeAmount: Int? = null
}