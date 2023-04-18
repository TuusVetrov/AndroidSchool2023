package com.example.hxh_project.domain.use_case

import com.example.hxh_project.data.remote.utils.ApiState
import com.example.hxh_project.data.repository.CatalogRepository
import com.example.hxh_project.domain.model.response.GetProductResponse
import javax.inject.Inject

class ProductUseCase @Inject constructor(
    private val catalogRepository: CatalogRepository
) {
    suspend fun getProductById(productId: String): ApiState<GetProductResponse> {
        return catalogRepository.getProductById(productId)
    }
}