package com.example.hxh_project.presentation.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val _uiState: MutableStateFlow<State<GetProductResponse>> = MutableStateFlow(State.init())
    val uiState: StateFlow<State<GetProductResponse>> = _uiState

    fun getProduct(id: String) {
        _uiState.update {
            State.loading()
        }
        viewModelScope.launch {
            try {
                val data = productUseCase.getProduct(id)

                data.getOrThrow().apply {
                    _uiState.update {
                        State.success(this)
                    }
                }
            } catch (e: java.lang.Exception) {
                _uiState.update {
                    State.error("")
                }
            }
        }
    }
}