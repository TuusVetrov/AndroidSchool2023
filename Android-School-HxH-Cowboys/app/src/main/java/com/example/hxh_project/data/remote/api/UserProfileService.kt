package com.example.hxh_project.data.remote.api

import com.example.hxh_project.domain.model.response.GetUserResponse
import retrofit2.Response
import retrofit2.http.GET

interface UserProfileService {
    @GET("user")
    suspend fun getProfile(): Response<GetUserResponse>
}