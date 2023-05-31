package com.example.hxh_project.utils

sealed class OrderStatusType {
    object All: OrderStatusType()
    object InWork: OrderStatusType()
    object Done: OrderStatusType()
    object Cancelled: OrderStatusType()
}