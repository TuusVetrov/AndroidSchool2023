package com.example.hxh_project.presentation.ui.orders

import com.example.hxh_project.domain.model.Order
import com.example.hxh_project.presentation.ui.sign_in.SignInState

data class OrderState(
    val orders: List<Order>,
    val isLoading: Boolean,
    val isEmpty: Boolean,
    val isUserLoggedIn: Boolean,
    val error: String?,
) {
    companion object {
        val initState = OrderState(
            orders = emptyList(),
            isLoading = false,
            isEmpty = false,
            isUserLoggedIn = false,
            error = null,
        )
    }
}