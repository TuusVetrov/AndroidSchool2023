package com.example.hxh_project.presentation.ui.orders

import com.example.hxh_project.domain.model.Order

sealed class OrderState {
    object Failure : OrderState()
    data class Success(val order: Order) : OrderState()
}