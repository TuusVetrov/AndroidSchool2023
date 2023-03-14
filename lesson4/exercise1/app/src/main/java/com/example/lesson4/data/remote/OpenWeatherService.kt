package com.example.lesson4.data.remote

import com.example.lesson4.data.entity.TemperatureData
import com.example.lesson4.utils.AppConstants.API_KEY
import com.example.lesson4.utils.AppConstants.BASE_URL
import com.example.lesson4.utils.AppConstants.WEATHER_UNIT
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherService {
    @GET("weather")
    suspend fun getTemperatureDataByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = WEATHER_UNIT,
        @Query("appid") apiKey: String = API_KEY,
    ): Response<TemperatureData>

    @GET("weather")
    suspend fun getTemperatureDataByCityName(
        @Query("q") cityName: String,
        @Query("units") units: String = WEATHER_UNIT,
        @Query("appid") apiKey: String = API_KEY,
    ): Response<TemperatureData>

    companion object {
        operator fun invoke(): OpenWeatherService {
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
                    .create(OpenWeatherService::class.java)
        }
    }
}