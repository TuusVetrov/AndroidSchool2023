package com.example.hxh_project.domain.use_case

import com.example.hxh_project.data.remote.utils.ApiState
import com.example.hxh_project.data.repository.CatalogRepository
import com.example.hxh_project.domain.model.response.GetProductsResponse
import javax.inject.Inject

class CatalogUseCase @Inject constructor(
    private val catalogRepository: CatalogRepository
) {
    suspend fun getProducts(): ApiState<GetProductsResponse> {
        return catalogRepository.getAllProducts()
    }
}