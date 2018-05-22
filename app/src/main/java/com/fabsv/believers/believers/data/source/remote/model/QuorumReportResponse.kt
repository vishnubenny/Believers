package com.fabsv.believers.believers.data.source.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class QuorumReportResponse {
    @SerializedName("ClergyCount")
    @Expose
    var clergyCount: Int? = null

    @SerializedName("ClergyPresentCount")
    @Expose
    var clergyPresentCount: Int? = null

    @SerializedName("LaymenCount")
    @Expose
    var laymenCount: Int? = null

    @SerializedName("LaymenPresentCount")
    @Expose
    var laymenPresentCount: Int? = null

    @SerializedName("TotalMemberCount")
    @Expose
    var totalMemberCount: Int? = null

    @SerializedName("PresentMemberCount")
    @Expose
    var presentMemberCount: Int? = null

    @SerializedName("QuorumPerc")
    @Expose
    var quorumPerc: Int? = null
}