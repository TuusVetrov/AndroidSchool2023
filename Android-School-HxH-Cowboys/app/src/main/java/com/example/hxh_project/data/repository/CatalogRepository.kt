package com.example.hxh_project.data.repository

import com.example.hxh_project.domain.model.Badge
import com.example.hxh_project.domain.model.Product
import com.example.hxh_project.domain.model.ProductSize
import com.example.hxh_project.domain.model.request.GetProductRequest
import com.example.hxh_project.domain.model.response.GetProductResponse
import com.example.hxh_project.domain.model.response.GetProductsResponse
import kotlinx.coroutines.delay

class CatalogRepository {
    private var products = mutableListOf<Product>()

    private val titles = mutableListOf(
        "Nike Tampa Bay Buccaneers Super Bowl LV",
        "Women's Refried Apparel Red/Pewter Tampa Bay Buccaneers Hooded Mini Dress And Som neers Hooded Mini Dress And Som"
    )

    private val productDescription = "The Tampa Bay Buccaneers are headed to Super Bowl LV! As a major fan, this is no surprise but it's definitely worth celebrating, especially after the unprecedented 2020 NFL season. Add this Tom Brady Game Jersey to your collection to ensure you're Super Bowl ready. This Nike gear features bold commemorative graphics that will let the Tampa Bay Buccaneers know they have the best fans in the league."

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
                department = category[it % category.size],
                previewImage = images[it % images.size],
                price = (1..99999).random(),
                badge = Badge(
                    value = "Хит сезона",
                    color = "#3C72BF"
                ),
                images = images,
                sizes = listOf(
                    ProductSize(
                        value = "S",
                        isAvailable = true,
                    ),
                    ProductSize(
                        value = "M",
                        isAvailable = false,
                    ),
                    ProductSize(
                        value = "L",
                        isAvailable = true,
                    ),
                    ProductSize(
                        value = "XL",
                        isAvailable = false,
                    )
                ),
                description = productDescription,
                details = listOf(
                    "Material: 100% Polyester.",
                    "Foam tongue helps reduce lace pressure.",
                    "Mesh in the upper provides a breathable and plush sensation that stretches with your foot.",
                    "Midfoot webbing delivers security. The webbing tightens around your foot when you lace up, letting you choose your fit and feel.",
                    "Nike React foam is lightweight, springy and durable. More foam means better cushioning without the bulk. A Zoom Air unit in the forefoot delivers more bounce with every step. It's top-loaded to be closer to your foot for responsiveness.",
                    "The classic fit and feel of the Pegasus is back—with a wider toe box to provide extra room. Seaming on the upper provides a better shape and fit, delivering a fresh take on an icon.",
                    "Officially licensed.",
                    "Imported.",
                    "Brand: Nike"
                )
            )
        } as MutableList<Product>
    }

    suspend fun getProducts(): Result<GetProductsResponse> {
        randomDelay()
        return randomResult(GetProductsResponse(products))
    }

    suspend fun getProduct(request: GetProductRequest): Result<GetProductResponse> {
        randomDelay()
        return randomResult(GetProductResponse(products[request.productId.toInt()]))
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