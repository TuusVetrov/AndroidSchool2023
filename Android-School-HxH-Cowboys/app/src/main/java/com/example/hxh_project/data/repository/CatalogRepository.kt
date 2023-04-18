package com.example.hxh_project.data.repository

import com.example.hxh_project.data.remote.api.AuthService
import com.example.hxh_project.data.remote.api.ProductService
import com.example.hxh_project.data.remote.utils.ApiState
import com.example.hxh_project.data.remote.utils.getResponse
import com.example.hxh_project.data.remote.utils.json
import com.example.hxh_project.domain.model.Badge
import com.example.hxh_project.domain.model.Product
import com.example.hxh_project.domain.model.ProductSize
import com.example.hxh_project.domain.model.request.AuthRequest
import com.example.hxh_project.domain.model.request.GetProductRequest
import com.example.hxh_project.domain.model.response.AuthResponse
import com.example.hxh_project.domain.model.response.GetOrdersResponse
import com.example.hxh_project.domain.model.response.GetProductResponse
import com.example.hxh_project.domain.model.response.GetProductsResponse
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration

@Singleton
class CatalogRepository @Inject constructor(
    private val productService: ProductService
) {
    suspend fun getAllProducts(): ApiState<GetProductsResponse> {
        return runCatching {
            val catalogResponse = productService.getProductList().getResponse()
            ApiState.success(catalogResponse)
        }.getOrElse { throwable ->
            ApiState.error(throwable.message ?: "Что-то пошло не так!")
        }
    }

    suspend fun getProductById(productId: String) : ApiState<GetProductResponse> {
        return runCatching {
            val productResponse = productService.getProduct(productId).getResponse()
            ApiState.success(productResponse)
        }.getOrElse { throwable ->
            ApiState.error(throwable.message ?: "Что-то пошло не так!")
        }
    }

    suspend fun getOrders() : ApiState<GetOrdersResponse> {
        return runCatching {
            val ordersResponse = productService.getOrders().getResponse()
            ApiState.success(ordersResponse)
        }.getOrElse { throwable ->
            ApiState.error(throwable.message ?: "Что-то пошло не так!")
        }
    }
}