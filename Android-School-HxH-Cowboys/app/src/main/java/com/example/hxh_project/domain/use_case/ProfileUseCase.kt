package com.example.hxh_project.domain.use_case

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.core.content.pm.PackageInfoCompat
import com.example.hxh_project.R
import com.example.hxh_project.data.remote.utils.ApiState
import com.example.hxh_project.data.model.response.GetUserResponse
import com.example.hxh_project.data.repository.UserRepository
import com.example.hxh_project.data.model.request.ChangeUserProfileRequest
import com.example.hxh_project.data.model.response.ChangeUserProfileResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProfileUseCase @Inject constructor(
    private val repository: UserRepository,
    @ApplicationContext private val context: Context,
) {
    fun getAppVersion(): String {
        val packageManager = context.packageManager
        val packageName = context.packageName
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            packageManager.getPackageInfo(packageName, 0)
        }

        return context.getString(
            R.string.app_version,
            packageInfo.versionName,
            PackageInfoCompat.getLongVersionCode(packageInfo).toString()
        )
    }

    suspend fun getProfile(): ApiState<GetUserResponse> {
        return repository.getProfile()
    }

    suspend fun changeUserPhoto(imageUrl: String): ApiState<Unit> {
        return repository.changeUserPhoto(imageUrl)
    }

    suspend fun changeUserProfile(
        userData: List<ChangeUserProfileRequest>
    ): ApiState<ChangeUserProfileResponse> {
        return repository.changeUserProfile(userData)
    }

    suspend fun getUserPhoto(id: String): ApiState<Bitmap> {
        return repository.getUserPhoto(id)
    }
}