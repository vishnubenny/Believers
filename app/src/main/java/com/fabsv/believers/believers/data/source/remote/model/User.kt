package com.fabsv.believers.believers.data.source.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class User {
    @SerializedName("status")
    @Expose
    private var status: String? = null

    fun setStatus(status: String) {
        this.status = status
    }

    fun getStatus(): String? {
        return status
    }
}
