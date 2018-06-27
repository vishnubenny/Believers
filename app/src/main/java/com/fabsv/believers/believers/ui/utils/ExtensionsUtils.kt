@file:JvmName("ExtensionsUtils")

package com.fabsv.believers.believers.ui.utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun Toast.setCustomToastProperties(textColor: Int = Color.WHITE, bgColor: Int? = null) {
    val textView = view.findViewById<TextView>(android.R.id.message)
    textView.apply {
        setTextColor(textColor)
        textScaleX = 1.2F
        gravity = Gravity.CENTER
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            letterSpacing = .1F
        }
    }
    bgColor?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.backgroundTintList = ColorStateList.valueOf(it)
        }
    }
}