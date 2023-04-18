package com.example.hxh_project.presentation.ui.catalog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hxh_project.R
import com.example.hxh_project.core.token_manager.TokenManager
import com.example.hxh_project.data.repository.CatalogRepository
import com.example.hxh_project.domain.use_case.CatalogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val catalogUseCase: CatalogUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState: MutableStateFlow<CatalogState> = MutableStateFlow(CatalogState.initState)
    val uiState: StateFlow<CatalogState> = _uiState

    init {
        checkUserLoggedIn()
    }

     fun getProducts() {
        viewModelScope.launch {
            val response = catalogUseCase.getProducts()

            response.onSuccess {
                if (it.data.isEmpty()){
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            isEmpty = true,
                            error = null
                        )
                    }
                }else {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            products = it.data,
                            error = null
                        )
                    }
                }
            }.onFailure {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = it
                    )
                }
            }
        }
    }

    private fun checkUserLoggedIn() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isUserLoggedIn = tokenManager.getToken() != null,
                )
            }
        }
    }
}
