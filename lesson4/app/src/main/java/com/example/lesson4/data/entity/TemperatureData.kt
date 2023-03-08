package com.example.lesson4.data.entity

data class TemperatureData(
    val main: Main
) {
    data class Main(
        val temp: Double
    )
}
