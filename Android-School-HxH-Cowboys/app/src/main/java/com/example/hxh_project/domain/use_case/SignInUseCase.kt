package com.example.hxh_project.domain.use_case

import com.example.hxh_project.data.remote.utils.ApiState
import com.example.hxh_project.domain.model.request.AuthRequest
import com.example.hxh_project.domain.model.response.AuthResponse
import com.example.hxh_project.data.repository.UserRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): ApiState<AuthResponse> {
        return repository.getUserByEmailAndPassword(email, password)
    }
}