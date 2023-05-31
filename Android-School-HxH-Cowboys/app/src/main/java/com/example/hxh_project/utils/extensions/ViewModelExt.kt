package com.example.hxh_project.utils.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

inline fun <T> ViewModel.updateStateProperty(
    uiState: MutableStateFlow<T>,
    crossinline block: suspend T.() -> T
) {
    viewModelScope.launch {
        uiState.update { currentState ->
            block(currentState)
        }
    }
}