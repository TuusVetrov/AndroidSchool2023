package com.example.hxh_project.domain.model.response

import com.example.hxh_project.domain.model.Order

data class GetOrdersResponse(
    var data: List<Order>,
)