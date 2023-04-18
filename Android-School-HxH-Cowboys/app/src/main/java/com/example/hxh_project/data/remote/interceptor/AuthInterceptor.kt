package com.example.hxh_project.data.remote.interceptor

import com.example.hxh_project.core.token_manager.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor  @Inject constructor(
    private val tokenManager: TokenManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val authRequest = chain.request().newBuilder().apply {
            tokenManager.getToken()?.let { header("Authorization", "Bearer $it") }
        }.build()
        return chain.proceed(authRequest)
    }
}