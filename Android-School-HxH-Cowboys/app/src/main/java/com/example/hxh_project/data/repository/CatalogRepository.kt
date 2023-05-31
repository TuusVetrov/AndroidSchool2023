package com.example.hxh_project.data.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.hxh_project.R
import com.example.hxh_project.data.datasource.OrdersDataSource
import com.example.hxh_project.data.datasource.ProductsDataSource
import com.example.hxh_project.data.remote.api.ProductService
import com.example.hxh_project.data.remote.utils.ApiState
import com.example.hxh_project.data.remote.utils.getResponse
import com.example.hxh_project.domain.model.Order
import com.example.hxh_project.domain.model.Product

import com.example.hxh_project.data.model.request.CheckOutRequest
import com.example.hxh_project.data.model.response.CheckOutResponse
import com.example.hxh_project.data.model.response.GetProductResponse
import com.example.hxh_project.utils.OrderStatusType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogRepository @Inject constructor(
    private val appContext: Context,
    private val productService: ProductService
) {
    fun getAllProducts(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ProductsDataSource(productService)
            }
        ).flow
    }

    fun getOrders(statusType: OrderStatusType): Flow<PagingData<Order>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                OrdersDataSource(productService, statusType)
            }
        ).flow
    }

    suspend fun cancelOrder(id: String) : ApiState<CheckOutResponse> {
        return runCatching {
            val productResponse = productService.cancelOrder(id)
            productResponse.getResponse()
        }.getOrDefault(ApiState.error(appContext.getString(R.string.error_on_cancel_order)))
    }

    suspend fun getProductById(productId: String) : ApiState<GetProductResponse> {
        return runCatching {
            val productResponse = productService.getProduct(productId)
            productResponse.getResponse()
        }.getOrDefault(ApiState.error(appContext.getString(R.string.unknown_error_message)))
    }

    suspend fun checkOut(
        productId: String,
        quantity: Int,
        size: String,
        house: String,
        apartment: String,
        etd: String
    ) : ApiState<CheckOutResponse> {
        return runCatching {
            val checkOutResponse = productService.checkOut(
                CheckOutRequest(
                productId, quantity, size, house, apartment,etd)
            )
            checkOutResponse.getResponse()
        }.getOrDefault(ApiState.error(appContext.getString(R.string.error_on_placing_an_order)))
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }
}