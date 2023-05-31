package com.example.hxh_project.utils.extensions

import com.google.android.material.textfield.TextInputLayout

inline fun TextInputLayout.setError(isError: Boolean, message: () -> String) {
    error = if (isError) message() else null
}
