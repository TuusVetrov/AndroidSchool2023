package com.example.hxh_project.domain.model.response

import com.example.hxh_project.domain.model.Product

data class GetProductsResponse(
    val products: List<Product>
)
