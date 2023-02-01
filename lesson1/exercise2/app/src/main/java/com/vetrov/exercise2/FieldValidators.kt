package com.vetrov.exercise2

import android.util.Patterns

object FieldValidators {
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidUrl(url: String): Boolean {
        val regex = "https?://[A-z 0-9]+\\.[A-z]{2,}[^\\d]+".toRegex()
        return url.matches(regex)
    }

    fun isValidTel(tel: String): Boolean {
        return Patterns.PHONE.matcher(tel).matches() //semi correct validator
    }

    fun isNumeric(str: String): Boolean {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return str.matches(regex)
    }
}