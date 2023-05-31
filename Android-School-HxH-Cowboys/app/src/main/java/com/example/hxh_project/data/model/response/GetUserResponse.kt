package com.example.hxh_project.data.model.response

import com.example.hxh_project.domain.model.Profile

data class GetUserResponse(
    val data: Data,
){
    data class Data(
        val profile: Profile
    )
}