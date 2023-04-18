package com.example.hxh_project.presentation.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hxh_project.domain.model.Order
import com.example.hxh_project.domain.use_case.OrdersUseCase
import com.example.hxh_project.utils.OrderStatusType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel@Inject constructor(
    private val ordersUseCase: OrdersUseCase,
): ViewModel() {
    private val _uiState: MutableStateFlow<OrderState> = MutableStateFlow(OrderState.initState)
    val uiState: StateFlow<OrderState> = _uiState

    fun getOrders(type: OrderStatusType) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                )
            }

            val response = ordersUseCase.getOrders(type)

            response.onSuccess { orders ->
                if (orders.data.isEmpty())
                {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            isEmpty = true,
                            error = null,
                        )
                    }
                }else {

                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            orders = orders.data,
                            error = null,
                        )
                    }
                }
            }.onFailure {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = null,
                    )
                }
            }
        }
    }

    suspend fun getOrdersNames(data: List<Order>) = coroutineScope {
        val tasks = data.map {
          //  async { ordersUseCase.getProductById(it.productId) }
        }

        //val products = tasks.awaitAll()

    }
}