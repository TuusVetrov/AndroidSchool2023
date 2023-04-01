package com.example.hxh_project.data.repository

import com.example.hxh_project.domain.model.request.AuthRequest
import com.example.hxh_project.domain.model.response.AuthResponse
import com.example.hxh_project.domain.model.response.GetUserResponse
import com.example.hxh_project.domain.model.Profile
import kotlinx.coroutines.delay

class UserRepository {
    private val profileResponse = GetUserResponse(
        name = "Анна",
        surname = "Виноградова",
        occupation = "Садовник",
        avatarUrl = "https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50?s=200"
    )

    private val authResponse = AuthResponse(
        token = "AAAAAAAAAAAAAAAAAAAAAFnz2wAAAAAACOwLSPtVT5gxxxxxxxxxxxx",
        profile = Profile(
            name = "Анна",
            surname = "Виноградова",
            occupation = "Садовник",
            avatarUrl = "https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50?s=200"
        )
    )

    suspend fun signIn(authData: AuthRequest): Result<AuthResponse> {
        randomDelay()
        return randomResult(authResponse)
    }

    suspend fun getUser(): Result<GetUserResponse> {
        randomDelay()
        return randomResult(profileResponse)
    }

    private suspend fun randomDelay() {
        delay((100L..1000L).random())
    }

    private fun <T> randomResult(data: T): Result<T> =
        if ((0..100).random() < 5) {
            Result.failure(RuntimeException())
        } else {
            Result.success(data)
        }
}