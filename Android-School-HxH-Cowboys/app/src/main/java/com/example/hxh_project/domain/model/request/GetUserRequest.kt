package com.example.hxh_project.domain.model.request

import com.example.hxh_project.domain.model.AuthCredential

data class GetUserRequest(
    val token: AuthCredential
)