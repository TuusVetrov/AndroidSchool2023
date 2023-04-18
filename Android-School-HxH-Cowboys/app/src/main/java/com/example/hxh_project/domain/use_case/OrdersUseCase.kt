package com.example.hxh_project.domain.use_case

import com.example.hxh_project.data.remote.utils.ApiState
import com.example.hxh_project.data.repository.CatalogRepository
import com.example.hxh_project.domain.model.response.GetOrdersResponse
import com.example.hxh_project.domain.model.response.GetProductResponse
import com.example.hxh_project.utils.OrderStatusType
import javax.inject.Inject

class OrdersUseCase @Inject constructor(
    private val catalogRepository: CatalogRepository
) {
    suspend fun getOrders(type: OrderStatusType): ApiState<GetOrdersResponse> {
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
}