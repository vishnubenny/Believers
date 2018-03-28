package com.fabsv.believers.believers.ui.utils

import android.content.Context
import android.support.v7.app.AlertDialog
import com.fabsv.believers.believers.ui.module.mainactivity.MainActivity

class AppAlertDialog {
    val context: Context
    val message: String
    val positiveButton: String
    val mainActivity1: MainActivity

    var mainActivity2: MainActivity? = null
    var negativeButton: String = ""

    var alertDialog: AlertDialog? = null


    constructor(context: Context, message: String, positiveButton: String, negativeButton: String,
                mainActivity1: MainActivity, mainActivity2: MainActivity) {
        this.context = context
        this.message = message
        this.positiveButton = positiveButton
        this.mainActivity1 = mainActivity1
        this.negativeButton = negativeButton
        this.mainActivity2 = mainActivity2
    }

    constructor(context: Context, message: String, positiveButton: String, mainActivity1: MainActivity) {
        this.context = context
        this.message = message
        this.positiveButton = positiveButton
        this.mainActivity1 = mainActivity1
    }


}
