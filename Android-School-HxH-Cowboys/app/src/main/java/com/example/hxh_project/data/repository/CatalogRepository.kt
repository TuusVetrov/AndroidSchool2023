package com.example.hxh_project.data.repository

import com.example.hxh_project.domain.model.Product
import kotlinx.coroutines.delay

class CatalogRepository {
    private var products = mutableListOf<Product>()

    private val titles = mutableListOf(
        "Nike Tampa Bay Buccaneers Super Bowl LV",
        "Women's Refried Apparel Red/Pewter Tampa Bay Buccaneers Hooded Mini Dress And Som neers Hooded Mini Dress And Som"
    )

    private val category = mutableListOf(
        "Джерси",
        "Невероятно длинный подзаголовок длинный подзаголовок"
    )

    private val images = mutableListOf(
        "https://images.unsplash.com/photo-1677131619017-0850d6dbcd8b?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=826&q=80",
        "https://plus.unsplash.com/premium_photo-1675812488919-21fc8fae565b?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=627&q=80",
        "https://images.unsplash.com/photo-1677131619088-81c39aeef242?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=790&q=80",
        "https://images.unsplash.com/photo-1648931770267-d925f21df971?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80",
        "https://images.unsplash.com/photo-1648931851212-3020920ba36e?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80"
    )

    init {
        titles.shuffle()
        category.shuffle()
        images.shuffle()
        products = (0..10).map {
            Product(
                id = it.toString(),
                title = titles[it % titles.size],
                category = category[it % category.size],
                previewImage = images[it % images.size],
                price = (1..99999).random()
            )
        } as MutableList<Product>
    }

    suspend fun getProducts(): Result<List<Product>> {
        randomDelay()
        return randomResult(products)
    }

    private suspend fun randomDelay() {
        delay((100L..1000L).random())
    }

    private fun <T> randomResult(data: T): Result<T> =
        if ((0..100).random() < 5) {
            Result.failure(RuntimeException())
        } else {
            Result.success(data)
        }
}