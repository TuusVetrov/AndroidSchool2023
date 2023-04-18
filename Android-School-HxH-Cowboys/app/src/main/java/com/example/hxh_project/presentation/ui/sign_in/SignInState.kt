package com.example.hxh_project.presentation.ui.sign_in

data class SignInState(
    val email: String,
    val password: String,
    val isLoading: Boolean,
    val isLoggedIn: Boolean,
    val isValidEmail: Boolean?,
    val isValidPassword: Boolean?,
    val error: String?,
) {
    companion object {
        val initState = SignInState(
            email = "",
            password = "",
            isLoading = false,
            isLoggedIn = false,
            isValidEmail = null,
            isValidPassword = null,
            error = null,
        )
    }
}