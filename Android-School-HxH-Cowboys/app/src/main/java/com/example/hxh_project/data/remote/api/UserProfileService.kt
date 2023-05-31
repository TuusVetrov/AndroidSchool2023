package com.example.hxh_project.data.remote.api

import com.example.hxh_project.data.model.request.ChangeUserProfileRequest
import com.example.hxh_project.data.model.response.ChangeUserProfileResponse
import com.example.hxh_project.data.model.response.GetUserResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface UserProfileService {
    @GET("user")
    suspend fun getProfile(): Response<GetUserResponse>

    @Multipart
    @POST("user/photo")
    suspend fun uploadUserPhoto(@Part multipartBody: MultipartBody.Part): Response<Unit>

    @GET("user/photo/{fileId}")
    suspend fun getUserPhoto(@Path("fileId") id: String): Response<ResponseBody>

    @PATCH("user")
    suspend fun changeUserProfile(
        @Body changeUserProfileRequest: List<ChangeUserProfileRequest>
    ): Response<ChangeUserProfileResponse>
}