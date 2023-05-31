package com.example.hxh_project.data.remote.api

import com.example.hxh_project.data.model.request.CheckOutRequest
import com.example.hxh_project.data.model.response.CheckOutResponse
import com.example.hxh_project.data.model.response.GetOrdersResponse
import com.example.hxh_project.data.model.response.GetProductResponse
import com.example.hxh_project.data.model.response.GetProductsResponse
import retrofit2.Response
import retrofit2.http.*

interface ProductService {
    @GET("products")
    suspend fun getProductList(
        @Query("pageNumber") page: Int,
        @Query("pageSize") size: Int = 10
    ): GetProductsResponse

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") productId: String): Response<GetProductResponse>

    @GET("orders")
    suspend fun getOrders(
        @Query("pageNumber") page: Int,
        @Query("pageSize") size: Int = 10
    ): GetOrdersResponse

    @PUT("orders/{orderId}")
    suspend fun cancelOrder(@Path("orderId") orderId: String): Response<CheckOutResponse>

    @POST("orders/checkout")
    suspend fun checkOut(@Body checkoutRequest: CheckOutRequest): Response<CheckOutResponse>
}