package com.example.hxh_project.data.remote.api

import com.example.hxh_project.data.model.request.AuthRequest
import com.example.hxh_project.data.model.response.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT

interface AuthService {
    @PUT("user/signin")
    suspend fun signIn(@Body authRequest: AuthRequest): Response<AuthResponse>
}