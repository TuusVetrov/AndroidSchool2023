package com.example.hxh_project.utils

sealed class State<T> {
    class Init<T> : State<T>()
    class Loading<T> : State<T>()
    data class Success<T>(val data: T) : State<T>()
    data class Error<T>(val message: String) : State<T>()

    companion object {
        fun <T> init() = Init<T>()
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> error(message: String) = Error<T>(message)
    }
}