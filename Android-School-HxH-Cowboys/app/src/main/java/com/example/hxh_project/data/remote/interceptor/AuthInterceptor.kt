package com.example.hxh_project.data.remote.interceptor

import com.example.hxh_project.data.token_manager.TokenManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor  @Inject constructor(
    private val tokenManager: TokenManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val accessToken = tokenManager.getToken()

        val authResponse = chain.proceed(newRequestWithAccessToken(accessToken, request))

        if (authResponse.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            val newAccessToken = tokenManager.getToken()
            return if (newAccessToken != accessToken) {
                chain.proceed(newRequestWithAccessToken(accessToken, request))
            } else {
                tokenManager.deleteToken()
                authResponse
            }
        }

        return authResponse
    }

    private fun newRequestWithAccessToken(accessToken: String?, request: Request): Request =
        request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
}