package com.example.hxh_project.presentation.ui.catalog

import androidx.paging.PagingData
import com.example.hxh_project.domain.model.Product
import kotlinx.coroutines.flow.Flow

data class CatalogState (
    val isLoading: Boolean,
    val isEmpty: Boolean,
    val products: Flow<PagingData<Product>>?,
    val error: String?,
    val isUserLoggedIn: Boolean?,
) {
    companion object {
        val initState = CatalogState(
            isLoading = false,
            isEmpty = false,
            products = null,
            error = null,
            isUserLoggedIn = null
        )
    }
}
