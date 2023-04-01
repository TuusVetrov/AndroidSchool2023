package com.example.hxh_project.domain.model.response

data class GetUserResponse(
    val name: String,
    val surname: String,
    val occupation: String,
    val avatarUrl: String,
)