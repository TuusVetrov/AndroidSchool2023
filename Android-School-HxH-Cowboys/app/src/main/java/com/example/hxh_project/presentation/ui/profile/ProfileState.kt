package com.example.hxh_project.presentation.ui.profile

import com.example.hxh_project.domain.model.Profile
import com.example.hxh_project.domain.model.response.GetUserResponse
import com.example.hxh_project.presentation.ui.sign_in.SignInState

data class ProfileState(
    val profile: Profile?,
    val appVersion: String,
    val isLoading: Boolean,
    val isUserLoggedIn: Boolean,
    val error: String?,
){
    companion object {
        val initState = ProfileState(
            profile = null,
            appVersion = "",
            isLoading = false,
            isUserLoggedIn = false,
            error = null,
        )
    }
}