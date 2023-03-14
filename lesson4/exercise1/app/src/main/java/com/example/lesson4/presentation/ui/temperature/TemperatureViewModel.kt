package com.example.lesson4.presentation.ui.temperature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lesson4.data.entity.TemperatureData
import com.example.lesson4.data.remote.OpenWeatherService
import com.example.lesson4.data.repository.TemperatureDataRepository
import com.example.lesson4.domain.usecases.GetTemperature
import com.example.lesson4.utils.Exceptions
import com.example.lesson4.utils.State
import com.example.lesson4.utils.isCoordinates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TemperatureViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<State<TemperatureData>?> = MutableStateFlow(null)
    val uiState: StateFlow<State<TemperatureData>?> = _uiState.asStateFlow()

    private val getTemperatureUseCase =
        GetTemperature(TemperatureDataRepository(OpenWeatherService()))

    fun fetchTemperature(input: String) {
        _uiState.update {
            State.loading()
        }
        viewModelScope.launch {
            try {
                val data = if(input.isCoordinates()) {
                    val (lat, lon) = input.split(",")
                    getTemperatureUseCase.byCoordinates(lat.toDouble(), lon.toDouble())
                } else {
                    getTemperatureUseCase.byCityName(input)
                }

                _uiState.update {
                    State.success(data)
                }
            }catch (e: Exceptions.ApiException){
                _uiState.update {
                    State.error(e.message ?: "")
                }
            }
        }
    }
}