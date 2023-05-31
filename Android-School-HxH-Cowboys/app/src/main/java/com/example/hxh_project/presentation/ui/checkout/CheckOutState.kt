package com.example.hxh_project.presentation.ui.checkout

import com.example.hxh_project.domain.model.Product
import com.example.hxh_project.data.model.response.CheckOutResponse

data class CheckOutState(
    val isLoading: Boolean,
    val order: CheckOutResponse?,
    val productId: String,
    val product: Product?,
    val productSize: String,
    val address: String,
    val apartment: String,
    val quantity: Int,
    val dateForView: String,
    val etd: String,
    val isValidSize: Boolean?,
    val isValidAddress: Boolean?,
    val isValidDate: Boolean?,
    val error: String?,
) {
    companion object {
        val initState = CheckOutState(
            isLoading = false,
            order = null,
            productId = "",
            productSize = "",
            address = "",
            apartment = "",
            quantity = 1,
            dateForView = "",
            etd = "",
            product = null,
            error = null,
            isValidSize = null,
            isValidAddress = null,
            isValidDate = null,
        )
    }
}
