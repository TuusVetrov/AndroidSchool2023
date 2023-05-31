package com.example.hxh_project.presentation.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.hxh_project.data.token_manager.TokenManager
import com.example.hxh_project.domain.model.Product
import com.example.hxh_project.domain.use_case.CatalogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    catalogUseCase: CatalogUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {
    val currentResult: Flow<PagingData<Product>> =
        catalogUseCase.getProducts().cachedIn(viewModelScope)

    private val _uiState: MutableStateFlow<CatalogState> = MutableStateFlow(CatalogState.initState)
    val uiState: StateFlow<CatalogState> = _uiState

    init {
        checkUserLoggedIn()
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
