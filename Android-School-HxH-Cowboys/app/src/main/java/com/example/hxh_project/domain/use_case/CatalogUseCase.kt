package com.example.hxh_project.domain.use_case

import androidx.paging.PagingData
import com.example.hxh_project.data.repository.CatalogRepository
import com.example.hxh_project.domain.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CatalogUseCase @Inject constructor(
    private val catalogRepository: CatalogRepository
) {
    fun getProducts(): Flow<PagingData<Product>> = catalogRepository.getAllProducts()
}