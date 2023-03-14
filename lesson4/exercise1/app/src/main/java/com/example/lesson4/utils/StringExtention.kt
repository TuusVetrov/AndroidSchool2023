package com.example.lesson4.utils

fun String.isCoordinates() =
    matches("([-+]?)(\\d{1,2})(((\\.)(\\d+)(,)))(\\s*)(([-+]?)([\\d]{1,3})((\\.)(\\d+))?)\$".toRegex())