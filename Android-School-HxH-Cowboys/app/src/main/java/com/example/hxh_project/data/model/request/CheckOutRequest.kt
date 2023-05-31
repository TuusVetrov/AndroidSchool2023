package com.example.hxh_project.data.model.request

data class CheckOutRequest(
    val productId: String,
    val quantity: Int,
    val size: String,
    val house: String,
    val apartment: String,
    val etd: String,
)