package com.example.hxh_project.presentation.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hxh_project.R
import com.example.hxh_project.data.repository.MockRepository
import com.example.hxh_project.domain.model.Product
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class UiState {
    object Loading: UiState()
    data class Notice(val imageId: Int,
                      val message: Int,
                      val description: Int,): UiState()
    data class Success(val data: List<Product>): UiState()
}

class CatalogViewModel: ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val mockRepository = MockRepository()

    private val handlerException = CoroutineExceptionHandler { _, throwable ->
        _uiState.update {
            UiState.Notice(
                R.drawable.img_logo,
                R.string.error_loading_title,
                R.string.error_loading_description
            )
        }
    }

    fun fetchData() {
        _uiState.update {
            UiState.Loading
        }
        viewModelScope.launch(handlerException) {
            val productsDeferred = async {
                mockRepository.getProducts()
            }

            val products = productsDeferred.await().getOrThrow()

            if (products.isNotEmpty()) {
                _uiState.update {
                    UiState.Success(products)
                }
            } else {
                _uiState.update {
                    UiState.Notice(
                        R.drawable.img_logo,
                        R.string.empty_state_title,
                        R.string.empty_state_description
                    )
                }
            }
        }
    }
}
