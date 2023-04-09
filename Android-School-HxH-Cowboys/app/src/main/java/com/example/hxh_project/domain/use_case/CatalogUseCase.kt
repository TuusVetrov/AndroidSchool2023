package com.example.hxh_project.domain.use_case

import com.example.hxh_project.data.repository.CatalogRepository
import com.example.hxh_project.domain.model.response.GetProductsResponse

class CatalogUseCase(
    private val repository: CatalogRepository
) {
    suspend fun getProducts(): Result<GetProductsResponse> {
        return repository.getProducts()
    }
}