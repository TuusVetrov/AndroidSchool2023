package com.example.lesson4.domain.usecases

import com.example.lesson4.data.entity.TemperatureData
import com.example.lesson4.data.repository.TemperatureDataRepository

class GetTemperature(
    private val dataRepository: TemperatureDataRepository
) {
    suspend fun byCityName(cityName: String): TemperatureData =
        dataRepository.getTemperatureByCityName(cityName)

    suspend fun byCoordinates(latitude: Double, longitude: Double): TemperatureData =
        dataRepository.getTemperatureByCoordinates(latitude, longitude)
}