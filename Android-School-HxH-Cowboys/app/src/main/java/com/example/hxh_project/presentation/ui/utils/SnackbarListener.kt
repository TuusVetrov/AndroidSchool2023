package com.example.hxh_project.presentation.ui.utils

interface SnackbarListener {
    fun showSuccess(message: String)
    fun showError(message: String)
}