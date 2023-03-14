package com.example.lesson4.data.repository

import com.example.lesson4.data.entity.TemperatureData
import com.example.lesson4.data.remote.OpenWeatherService
import com.example.lesson4.data.remote.SafeApiRequest

class TemperatureDataRepository(
    private val service: OpenWeatherService,
) : SafeApiRequest() {
    suspend fun getTemperatureByCoordinates(latitude: Double, longitude: Double): TemperatureData =
        apiRequest {
            service.getTemperatureDataByCoordinates(latitude, longitude)
        }

    suspend fun getTemperatureByCityName(cityName: String): TemperatureData =
        apiRequest {
            service.getTemperatureDataByCityName(cityName)
        }
}