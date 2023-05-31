package com.example.hxh_project.domain.use_case

import androidx.paging.PagingData
import com.example.hxh_project.data.remote.utils.ApiState
import com.example.hxh_project.data.repository.CatalogRepository
import com.example.hxh_project.domain.model.Order
import com.example.hxh_project.domain.model.Product
import com.example.hxh_project.data.model.response.CheckOutResponse
import com.example.hxh_project.data.model.response.GetOrdersResponse
import com.example.hxh_project.data.model.response.GetProductResponse
import com.example.hxh_project.utils.OrderStatusType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrdersUseCase @Inject constructor(
    private val catalogRepository: CatalogRepository
) {
   /* suspend fun getOrders(type: OrderStatusType): ApiState<GetOrdersResponse> {
        return if (type == OrderStatusType.All) {
            catalogRepository.getOrders()
        } else {
            catalogRepository.getOrders().onSuccess { response ->
                response.data = response.data.filter {
                    when (type) {
                        OrderStatusType.InWork -> it.status == "in_work"
                        OrderStatusType.Done -> it.status == "done"
                        else -> it.status == "cancelled"
                    }
                }
            }
        }
    }

    */

    fun getOrders(type: OrderStatusType): Flow<PagingData<Order>> = catalogRepository.getOrders(type)

   suspend fun cancelOrder(id: String): ApiState<CheckOutResponse> {
       return catalogRepository.cancelOrder(id)
   }

    suspend fun getProductById(productId: String): ApiState<GetProductResponse> {
        return catalogRepository.getProductById(productId)
    }

    suspend fun checkOut(
        productId: String,
        quantity: Int,
        size: String,
        house: String,
        apartment: String,
        etd: String
    ): ApiState<CheckOutResponse> {
        return catalogRepository.checkOut(productId, quantity, size, house, apartment,etd)
    }
}