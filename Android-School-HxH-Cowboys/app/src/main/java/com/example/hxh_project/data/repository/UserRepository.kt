package com.example.hxh_project.data.repository

import com.example.hxh_project.data.remote.api.AuthService
import com.example.hxh_project.data.remote.api.UserProfileService
import com.example.hxh_project.data.remote.utils.ApiState
import com.example.hxh_project.data.remote.utils.getResponse
import com.example.hxh_project.domain.model.request.AuthRequest
import com.example.hxh_project.domain.model.response.AuthResponse
import com.example.hxh_project.domain.model.response.GetUserResponse
import com.example.hxh_project.domain.model.Profile
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val authService: AuthService,
    private val userProfileService: UserProfileService,
) {
    suspend fun getUserByEmailAndPassword(
        email: String,
        password: String,
    ): ApiState<AuthResponse> {
        return runCatching {
            val authResponse = authService.signIn(AuthRequest(email, password)).getResponse()
            ApiState.success(authResponse)
        }.getOrElse { throwable ->
            ApiState.error(throwable.message ?: "Что-то пошло не так!")
        }
    }

    suspend fun getProfile(): ApiState<GetUserResponse> {
        return runCatching {
            val profileResponse = userProfileService.getProfile().getResponse()
            ApiState.success(profileResponse)
        }.getOrElse { throwable ->
            ApiState.error(throwable.message ?: "Что-то пошло не так!")
        }
    }
}