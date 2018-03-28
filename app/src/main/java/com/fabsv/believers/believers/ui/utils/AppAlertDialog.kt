package com.fabsv.believers.believers.ui.utils

import android.content.Context
import android.support.v7.app.AlertDialog

class AppAlertDialog {

    private val context: Context
    private val message: String
    private val positiveButton: String
    private var onAlertButtonClick: OnAlertButtonClick
    private var negativeButton: String = ""
    private var alertDialog: AlertDialog? = null


    constructor(context: Context, message: String, positiveButton: String, negativeButton: String,
                onAlertButtonClick: OnAlertButtonClick) {
        this.context = context
        this.message = message
        this.positiveButton = positiveButton
        this.negativeButton = negativeButton
        this.onAlertButtonClick = onAlertButtonClick
    }

    constructor(context: Context, message: String, positiveButton: String, onAlertButtonClick: OnAlertButtonClick) {
        this.context = context
        this.message = message
        this.positiveButton = positiveButton
        this.onAlertButtonClick = onAlertButtonClick
    }

    fun getAlertDialog(setCancelable: Boolean): AlertDialog {
        alertDialog = AlertDialog.Builder(context).create()
        alertDialog!!.setTitle(message)
        alertDialog!!.setButton(AlertDialog.BUTTON_POSITIVE, positiveButton,
                { dialog, which ->
                    dialog.dismiss()
                    onAlertButtonClick.onPositiveButtonClick()
                })
        if (negativeButton.isNotEmpty()) {
            alertDialog!!.setButton(AlertDialog.BUTTON_NEGATIVE, negativeButton,
                    { dialog, which ->
                        dialog.dismiss()
                        onAlertButtonClick.onNegativeButtonClick()
                    })
        }
        alertDialog!!.setCancelable(setCancelable)
        return alertDialog!!
    }

    interface OnAlertButtonClick {
        fun onPositiveButtonClick()
        fun onNegativeButtonClick()
    }
}
