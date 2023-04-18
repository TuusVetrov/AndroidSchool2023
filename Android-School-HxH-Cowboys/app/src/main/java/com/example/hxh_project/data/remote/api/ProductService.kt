package com.example.hxh_project.data.remote.api

import com.example.hxh_project.domain.model.response.GetOrdersResponse
import com.example.hxh_project.domain.model.response.GetProductResponse
import com.example.hxh_project.domain.model.response.GetProductsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductService {
    @GET("products")
    suspend fun getProductList(): Response<GetProductsResponse>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") productId: String): Response<GetProductResponse>

    @GET("orders")
    suspend fun getOrders(): Response<GetOrdersResponse>

    @PUT("orders/{orderId}")
    suspend fun cancelOrder(@Path("orderId") orderId: String)
}