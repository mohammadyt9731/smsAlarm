package com.ddt.smsalarm.util

import com.google.android.material.textfield.TextInputLayout


fun TextInputLayout.setOrClearError(text: String) {
    if (text.isNotBlank()) {
        error = text
        errorIconDrawable = null
    } else
        isErrorEnabled = false
}


