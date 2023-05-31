package com.example.hxh_project.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object FormatUtils {
    fun formatDate(time: String): Pair<String,String> {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val dateTime = LocalDateTime.parse(time, formatter)
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val dateStr = dateFormatter.format(dateTime)
        val timeStr = timeFormatter.format(dateTime)
        return Pair(dateStr, timeStr)
    }

    fun millisToIsoDateTime(millis: Long): String {
        val selectedDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(millis),
            ZoneId.systemDefault()
        )
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        return selectedDateTime.format(formatter)
    }

    fun getTimestampString(): String {
        val date = Calendar.getInstance()
        return SimpleDateFormat("yyyy MM dd hh mm ss", Locale.US).format(date.time).replace(" ", "")
    }

    fun millisToDate(millis: Long): String {
        val date = Date(millis)
        val dateFormat = SimpleDateFormat("d MMMM", Locale("ru"))
        return dateFormat.format(date)
    }

    fun priceFormat(price: Int): String {
        return String.format("%,d ₽", price).replace(",", " ")
    }
}