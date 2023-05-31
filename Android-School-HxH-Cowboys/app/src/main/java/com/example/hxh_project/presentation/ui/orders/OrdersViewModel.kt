package com.example.hxh_project.presentation.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.hxh_project.domain.use_case.OrdersUseCase
import com.example.hxh_project.utils.OrderStatusType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel@Inject constructor(
    private val ordersUseCase: OrdersUseCase,
): ViewModel() {
    val allOrders = ordersUseCase.getOrders(OrderStatusType.All).cachedIn(viewModelScope)
    val activeOrders = ordersUseCase.getOrders(OrderStatusType.InWork).cachedIn(viewModelScope)

    private val _cancelOrderResultFlow = MutableSharedFlow<OrderState>()
    val cancelOrderResultFlow: SharedFlow<OrderState> = _cancelOrderResultFlow

    fun cancelOrder(id: String) {
        viewModelScope.launch {
            val response = ordersUseCase.cancelOrder(id)
            response.onSuccess {

                _cancelOrderResultFlow.emit(OrderState.Success(it.data))
            }.onFailure {
                _cancelOrderResultFlow.emit(OrderState.Failure)
            }
        }
    }

}