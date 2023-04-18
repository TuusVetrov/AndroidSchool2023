package com.example.hxh_project.di

import com.example.hxh_project.data.remote.Constants
import com.example.hxh_project.data.remote.api.AuthService
import com.example.hxh_project.data.remote.api.ProductService
import com.example.hxh_project.data.remote.api.UserProfileService
import com.example.hxh_project.data.remote.interceptor.AuthInterceptor
import com.example.hxh_project.data.remote.utils.moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    private val baseRetrofitBuilder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(Constants.API_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))

    private val okHttpClientBuilder: OkHttpClient.Builder =
        OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)

    @Provides
    fun provideProductService(authInterceptor: AuthInterceptor): ProductService {
        return baseRetrofitBuilder
            .client(okHttpClientBuilder.addInterceptor(authInterceptor).build())
            .build()
            .create(ProductService::class.java)
    }

    @Provides
    fun provideUserProfileService(authInterceptor: AuthInterceptor): UserProfileService {
        return baseRetrofitBuilder
            .client(okHttpClientBuilder.addInterceptor(authInterceptor).build())
            .build()
            .create(UserProfileService::class.java)
    }

    @Provides
    fun provideAuthService(): AuthService {
        return baseRetrofitBuilder
            .build()
            .create(AuthService::class.java)
    }
}