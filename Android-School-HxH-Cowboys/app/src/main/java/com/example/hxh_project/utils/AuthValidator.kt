package com.example.hxh_project.utils

import android.util.Patterns

object AuthValidator {
    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 8;
    }
}