package com.example.hxh_project.presentation.ui.product

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hxh_project.data.remote.utils.json
import com.example.hxh_project.data.repository.CatalogRepository
import com.example.hxh_project.domain.model.response.GetProductResponse
import com.example.hxh_project.domain.use_case.ProductUseCase
import com.example.hxh_project.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productUseCase: ProductUseCase,
) : ViewModel() {
    private val _uiState: MutableStateFlow<ProductState> = MutableStateFlow(ProductState.initState)
    val uiState: StateFlow<ProductState> = _uiState

    fun setProductId(id: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    productId = id,
                )
            }
        }
    }

    fun getProduct() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                )
            }

            val response = productUseCase.getProductById(uiState.value.productId)

            response.onSuccess { productResponse ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        product = productResponse.data,
                        error = null,
                    )
                }
            }.onFailure { message ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = message,
                    )
                }
            }
        }
    }
}