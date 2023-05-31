package com.example.hxh_project.data.model.response

data class ErrorResponse(
    val error: Error
){
    data class Error(
        val message: String
    )
}