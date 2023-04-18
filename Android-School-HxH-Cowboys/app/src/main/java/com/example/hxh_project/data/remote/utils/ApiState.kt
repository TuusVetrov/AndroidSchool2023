package com.example.hxh_project.data.remote.utils

sealed class ApiState<T> {
    data class Success<T>(val data: T) : ApiState<T>()
    data class Error<T>(val message: String) : ApiState<T>()

    companion object {
        fun <T> success(data: T) = Success(data)
        fun <T> error(message: String) = Error<T>(message)
    }

    inline fun onSuccess(block: (T) -> Unit): ApiState<T> = apply {
        if (this is Success) {
            block(data)
        }
    }

    inline fun onFailure(block: (String) -> Unit): ApiState<T> = apply {
        if (this is Error) {
            block(message)
        }
    }
}