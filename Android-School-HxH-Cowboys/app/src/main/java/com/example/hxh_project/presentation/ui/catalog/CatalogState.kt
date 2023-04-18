package com.example.hxh_project.presentation.ui.catalog

import com.example.hxh_project.domain.model.Product

data class CatalogState (
    val isLoading: Boolean,
    val isEmpty: Boolean,
    val products: List<Product>,
    val error: String?,
    val isUserLoggedIn: Boolean?,
) {
    companion object {
        val initState = CatalogState(
            isLoading = false,
            isEmpty = false,
            products = emptyList(),
            error = null,
            isUserLoggedIn = null
        )
    }
}
