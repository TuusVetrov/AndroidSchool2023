package com.example.hxh_project.domain.model

data class Order(
    val id: String,
    val number: Int,
    val productId: String,
    val productPreview: String,
    val productQuantity: Int,
    val productSize: String,
    val createdAt: String,
    val etd: String,
    val deliveryAddress: String,
    val status: String,
)