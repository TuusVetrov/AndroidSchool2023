package com.example.hxh_project.data.model.request

data class ChangeUserProfileRequest(
    val path: String,
    private val op: String = OPERATION,
    val value: String,
){
    companion object {
        const val OPERATION = "replace" // delete or other operation not needed
    }
}