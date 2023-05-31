package com.example.hxh_project.presentation.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hxh_project.domain.use_case.OrdersUseCase
import com.example.hxh_project.utils.extensions.updateStateProperty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckOutViewModel @Inject constructor(
    private val useCase: OrdersUseCase,
) : ViewModel() {
    private val _uiState: MutableStateFlow<CheckOutState> = MutableStateFlow(CheckOutState.initState)
    val uiState: StateFlow<CheckOutState> = _uiState

    fun setProductId(id: String) =
        updateStateProperty(_uiState) { copy( productId = id ) }

    fun setProductSize(size: String) =
        updateStateProperty(_uiState) { copy( productSize = size ) }

    fun setQuantity(count: Int) =
        updateStateProperty(_uiState) { copy( quantity = count ) }

    fun setDateForView(time: String) =
        updateStateProperty(_uiState) { copy( dateForView = time ) }

    fun setEtd(time: String) =
        updateStateProperty(_uiState) { copy( etd = time ) }

    fun setApartment(number: String) =
        updateStateProperty(_uiState) { copy( apartment = number ) }

    fun setAddress(address: String) =
        updateStateProperty(_uiState) { copy( address = address ) }

    fun getProduct() {
        viewModelScope.launch {
            updateStateProperty(_uiState) { copy( isLoading = true ) }

            val response = useCase.getProductById(uiState.value.productId)

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

    fun checkOut() {
        if (!validateCredentials()) {
            return
        }

        viewModelScope.launch {
            updateStateProperty(_uiState) { copy( isLoading = true ) }

            val response = useCase.checkOut(
                    uiState.value.productId,
                    uiState.value.quantity,
                    uiState.value.productSize,
                    uiState.value.address,
                    uiState.value.apartment,
                    uiState.value.etd
                    )

            response.onSuccess { newOrder ->
                updateStateProperty(_uiState) {
                    copy(
                        isLoading = false,
                        order = newOrder,
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

    private fun validateCredentials(): Boolean {
        val isValidSize = uiState.value.productSize.trim().isNotEmpty()
        val isValidAddress = uiState.value.address.trim().isNotEmpty()
        val isValidDate = uiState.value.etd.trim().isNotEmpty()

        updateStateProperty(_uiState) {
            copy(
                isValidSize = isValidSize,
                isValidAddress = isValidAddress,
                isValidDate = isValidDate
            )
        }

        return isValidSize && isValidAddress && isValidDate
    }
}