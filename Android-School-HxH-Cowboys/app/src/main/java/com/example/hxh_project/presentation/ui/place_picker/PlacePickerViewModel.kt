package com.example.hxh_project.presentation.ui.place_picker

import androidx.lifecycle.ViewModel
import com.example.hxh_project.utils.extensions.updateStateProperty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PlacePickerViewModel @Inject constructor() : ViewModel() {
    private val _uiState: MutableStateFlow<String?> = MutableStateFlow(null)
    val uiState: StateFlow<String?> = _uiState

    fun setAddress(address: String?) = updateStateProperty(_uiState) { address }
}