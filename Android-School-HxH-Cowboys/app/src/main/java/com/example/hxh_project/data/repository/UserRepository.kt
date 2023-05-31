package com.example.hxh_project.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.hxh_project.R
import com.example.hxh_project.data.remote.api.AuthService
import com.example.hxh_project.data.remote.api.UserProfileService
import com.example.hxh_project.data.remote.utils.ApiState
import com.example.hxh_project.data.remote.utils.getResponse
import com.example.hxh_project.data.model.request.AuthRequest
import com.example.hxh_project.data.model.request.ChangeUserProfileRequest
import com.example.hxh_project.data.model.response.AuthResponse
import com.example.hxh_project.data.model.response.ChangeUserProfileResponse
import com.example.hxh_project.data.model.response.GetUserResponse
import com.example.hxh_project.utils.extensions.toMultiPart
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val appContext: Context,
    private val authService: AuthService,
    private val userProfileService: UserProfileService,
) {
    suspend fun getUserByEmailAndPassword(
        email: String,
        password: String,
    ): ApiState<AuthResponse> {
        return runCatching {
            val authResponse = authService.signIn(AuthRequest(email, password))
            authResponse.getResponse()
        }.getOrDefault(ApiState.error(appContext.getString(R.string.error_on_sign_in)))
    }

    suspend fun getProfile(): ApiState<GetUserResponse> {
        return runCatching {
            val profileResponse = userProfileService.getProfile()
            profileResponse.getResponse()
        }.getOrDefault(ApiState.error(appContext.getString(R.string.error_on_loading_profile)))
    }

    suspend fun changeUserPhoto(imageUrl: String): ApiState<Unit> {
        return runCatching {
            val file = File(imageUrl)
            val multipart = file.toMultiPart()
            userProfileService.uploadUserPhoto(multipart)
            return ApiState.success(Unit)
        }.getOrDefault(ApiState.error(appContext.getString(R.string.error_on_update_profile)))
    }

    suspend fun changeUserProfile(
        userData: List<ChangeUserProfileRequest>
    ): ApiState<ChangeUserProfileResponse> {
        return runCatching {
            val profileResponse = userProfileService.changeUserProfile(userData)
            profileResponse.getResponse()
        }.getOrDefault(ApiState.error(appContext.getString(R.string.error_on_update_profile)))
    }

    suspend fun getUserPhoto(id: String): ApiState<Bitmap> {
        return runCatching {
            val photoResponse = userProfileService.getUserPhoto(id)
            val image = BitmapFactory.decodeStream(photoResponse.body()?.byteStream())
            ApiState.success(image)
        }.getOrElse {
            ApiState.error(appContext.getString(R.string.error_on_loading_image_profile))
        }
    }
}