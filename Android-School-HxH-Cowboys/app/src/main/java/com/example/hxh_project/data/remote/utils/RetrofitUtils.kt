package com.example.hxh_project.data.remote.utils

import android.util.Log
import com.example.hxh_project.data.model.response.ErrorResponse
import retrofit2.Response

inline fun <reified T> Response<T>.getResponse(): ApiState<T> {
    val responseBody = body()
    return if (this.isSuccessful && responseBody != null) {
        ApiState.success(responseBody)
    } else {
        val errorResponse = moshi.adapter(ErrorResponse::class.java).fromJson(errorBody()?.string().toString())
        ApiState.error(errorResponse?.error?.message ?: "Что-то пошло не так!")
    }
}
