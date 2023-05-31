package com.example.hxh_project.presentation.ui.profile

import android.graphics.Bitmap
import com.example.hxh_project.domain.model.Profile
import com.example.hxh_project.data.model.response.GetUserResponse
import com.example.hxh_project.presentation.ui.sign_in.SignInState

data class ProfileState(
    val profile: Profile?,
    val userImage: Bitmap?,
    val appVersion: String,
    val isLoading: Boolean,
    val isUserLoggedIn: Boolean,
    val error: String?,
){
    companion object {
        val initState = ProfileState(
            profile = null,
            userImage = null,
            appVersion = "",
            isLoading = false,
            isUserLoggedIn = false,
            error = null,
        )
    }
}