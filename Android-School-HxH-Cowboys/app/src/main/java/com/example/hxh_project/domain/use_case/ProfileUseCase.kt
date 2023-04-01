package com.example.hxh_project.domain.use_case

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.pm.PackageInfoCompat
import com.example.hxh_project.domain.model.response.GetUserResponse
import com.example.hxh_project.data.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext

class ProfileUseCase(
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
        return "${packageInfo.versionName} (${PackageInfoCompat.getLongVersionCode(packageInfo)})"
    }

    suspend fun getProfile(): Result<GetUserResponse> {
        return repository.getUser()
    }

    suspend fun logOut(): Boolean {
        //TODO: delete access from storage
        return true
    }
}