package com.example.hxh_project.domain.model

data class Product(
    val id: String,
    val title: String,
    val department: String,
    val price: Int,
    val badge: List<Badge>,
    val preview: String,
    val images: List<String>,
    val sizes: List<ProductSize>,
    val description: String,
    val details: List<String>
)