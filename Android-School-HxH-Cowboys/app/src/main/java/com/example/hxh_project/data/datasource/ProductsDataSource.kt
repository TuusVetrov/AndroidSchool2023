package com.example.hxh_project.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.hxh_project.data.datasource.Constants.STARTING_PAGE_INDEX
import com.example.hxh_project.data.remote.api.ProductService
import com.example.hxh_project.domain.model.Product

class ProductsDataSource(
    private val productService: ProductService
): PagingSource<Int, Product>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = productService.getProductList(page)
            val products = response.data
            LoadResult.Page(
                data = products,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (products.isNotEmpty()) page + 1 else null
            )
        }catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return null
    }
}