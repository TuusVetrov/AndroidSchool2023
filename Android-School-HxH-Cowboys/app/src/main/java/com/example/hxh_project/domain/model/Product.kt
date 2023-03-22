package com.example.hxh_project.domain.model

data class Product(
    val id: String,
    val title: String,
    val category: String,
    val price: Int,
    val previewImage: String,
) {
}