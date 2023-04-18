package com.example.hxh_project.presentation.ui.product

import com.example.hxh_project.domain.model.Product

data class ProductState(
    val isLoading: Boolean,
    val productId: String,
    val product: Product?,
    val error: String?,
    val isUserLoggedIn: Boolean?,
) {
    companion object {
        val initState = ProductState(
            isLoading = false,
            productId = "",
            product = null,
            error = null,
            isUserLoggedIn = null
        )
    }
}