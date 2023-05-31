package com.example.hxh_project.utils.validators

import android.util.Patterns

object AuthValidator {
    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= MIN_PASSWORD_LENGTH;
    }

    private const val MIN_PASSWORD_LENGTH = 8
}