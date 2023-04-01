package com.example.hxh_project.domain.model.response

import com.example.hxh_project.domain.model.Profile

data class AuthResponse(
    val token: String?,
    val profile: Profile,
)
