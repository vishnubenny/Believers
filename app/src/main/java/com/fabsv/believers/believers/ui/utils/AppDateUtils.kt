package com.fabsv.believers.believers.ui.utils

import com.fabsv.believers.believers.util.constants.AppConstants
import java.text.SimpleDateFormat
import java.util.*

class AppDateUtils {

    companion object {
        fun getDateTimeObject(date: Date): String {
            val simpleDateFormat = SimpleDateFormat(AppConstants.DateConstants.DATE_TIME_OBJECT_FORMAT, Locale.getDefault())
            return simpleDateFormat.format(date)
        }
    }
}
