package com.example.hxh_project.domain.use_case

import com.example.hxh_project.domain.model.request.AuthRequest
import com.example.hxh_project.domain.model.response.AuthResponse
import com.example.hxh_project.data.repository.UserRepository

class SignInUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(login: String, password: String): Result<AuthResponse> {
        return repository.signIn(AuthRequest(login, password))
    }
}