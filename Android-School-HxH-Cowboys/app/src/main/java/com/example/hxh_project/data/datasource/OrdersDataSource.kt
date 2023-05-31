package com.example.hxh_project.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.hxh_project.data.remote.api.ProductService
import com.example.hxh_project.domain.model.Order
import com.example.hxh_project.utils.OrderStatusType

class OrdersDataSource(
    private val productService: ProductService,
    private val orderType: OrderStatusType,
): PagingSource<Int, Order>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Order> {
        val page = params.key ?: Constants.STARTING_PAGE_INDEX
        return try {
            val response = productService.getOrders(page)
            val products = response.data

            val data = if (orderType == OrderStatusType.All) {
                response.data
            } else {
                response.data.filter {
                    when (orderType) {
                        OrderStatusType.InWork -> it.status == STATUS_IN_WORK
                        OrderStatusType.Done -> it.status == STATUS_DONE
                        else -> it.status == STATUS_CANCELLED
                    }
                }
            }

            LoadResult.Page(
                data = data,
                prevKey = if (page == Constants.STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (products.isNotEmpty()) page + 1 else null
            )
        }catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Order>): Int? {
        return null
    }

    companion object {
        private const val STATUS_IN_WORK = "in_work"
        private const val STATUS_DONE = "done"
        private const val STATUS_CANCELLED = "cancelled"
    }
}