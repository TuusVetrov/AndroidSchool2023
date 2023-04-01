package com.example.hxh_project.domain.model.request

data class AuthRequest(
    val login: String,
    val password: String,
)