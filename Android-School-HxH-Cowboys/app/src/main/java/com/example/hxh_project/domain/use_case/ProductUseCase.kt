package com.example.hxh_project.domain.use_case

import com.example.hxh_project.data.repository.CatalogRepository
import com.example.hxh_project.data.repository.UserRepository
import com.example.hxh_project.domain.model.request.GetProductRequest
import com.example.hxh_project.domain.model.response.GetProductResponse
import com.example.hxh_project.domain.model.response.GetUserResponse

class ProductUseCase(
    private val repository: CatalogRepository
) {
    suspend fun getProduct(productId: String): Result<GetProductResponse> {
        return repository.getProduct(GetProductRequest(productId))
    }
}