package com.example.hxh_project.presentation.ui.settings

import android.graphics.Bitmap
import android.net.Uri

data class SettingsState(
    val name: String,
    val surname: String,
    val job: String,
    val userImage: Bitmap?,
    val isUserLoggedIn: Boolean,
    val newImageUrl: String,
    val isSuccessfullyModified: Boolean,
    val isValidName: Boolean?,
    val isValidSurname: Boolean?,
    val isValidJob: Boolean?,
    val isLoading: Boolean,
    val error: String?,
){
    companion object {
        val initState = SettingsState(
            name = "",
            surname = "",
            job = "",
            isLoading = false,
            userImage = null,
            isUserLoggedIn = false,
            newImageUrl = "",
            isSuccessfullyModified = false,
            isValidName = null,
            isValidSurname = null,
            isValidJob = null,
            error = null,
        )
    }
}