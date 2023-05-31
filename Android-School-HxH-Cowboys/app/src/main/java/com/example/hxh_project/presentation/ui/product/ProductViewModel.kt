package com.example.hxh_project.presentation.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hxh_project.domain.use_case.ProductUseCase
import com.example.hxh_project.utils.extensions.updateStateProperty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productUseCase: ProductUseCase,
) : ViewModel() {
    private val _uiState: MutableStateFlow<ProductState> = MutableStateFlow(ProductState.initState)
    val uiState: StateFlow<ProductState> = _uiState

    fun setProductId(id: String) =
        updateStateProperty(_uiState) { copy( productId = id ) }

    fun setSize(size: String?) =
        updateStateProperty(_uiState) { copy( size = size ) }


    fun getProduct() {
        viewModelScope.launch {
            updateStateProperty(_uiState) { copy(  isLoading = true ) }

            val response = productUseCase.getProductById(uiState.value.productId)

            response.onSuccess { productResponse ->
                updateStateProperty(_uiState) {
                    copy(
                        isLoading = false,
                        product = productResponse.data,
                        error = null,
                    )
                }
            }.onFailure { message ->
                updateStateProperty(_uiState) {
                    copy(
                        isLoading = false,
                        error = message,
                    )
                }
            }
        }
    }
}