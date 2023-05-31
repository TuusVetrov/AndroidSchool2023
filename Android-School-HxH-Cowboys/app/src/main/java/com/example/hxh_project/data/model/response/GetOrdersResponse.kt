package com.example.hxh_project.data.model.response

import com.example.hxh_project.domain.model.Order

data class GetOrdersResponse(
    var data: List<Order>,
)