package com.example.flyingaround.utils

import android.view.View
import android.widget.TextView

fun View.toggleVisibility() {
    visibility = if (visibility == View.VISIBLE) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

fun TextView.toggleSelection() {
    isSelected = !isSelected
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.changeVisibility(isVisible: Boolean) {
    if (isVisible) show() else hide()
}
