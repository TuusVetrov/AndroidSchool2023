package com.example.hxh_project.domain.model.response

import com.example.hxh_project.domain.model.Profile

data class AuthResponse(
    val data: Data
) {
    data class Data(
        val accessToken: String,
        val profile: Profile,
    )
}