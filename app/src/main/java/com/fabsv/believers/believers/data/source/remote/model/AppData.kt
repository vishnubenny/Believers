package com.fabsv.believers.believers.data.source.remote.model

class AppData<T> {
    var data: T? = null
    fun isSuccessful() = null != data
}