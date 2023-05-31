package com.example.hxh_project.di

import android.content.Context
import com.example.hxh_project.data.remote.api.AuthService
import com.example.hxh_project.data.remote.api.ProductService
import com.example.hxh_project.data.remote.api.UserProfileService
import com.example.hxh_project.data.repository.CatalogRepository
import com.example.hxh_project.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideUserRepository(
        @ApplicationContext appContext: Context,
        authService: AuthService,
        userProfileService: UserProfileService
    ): UserRepository = UserRepository(appContext,authService, userProfileService)

    @Provides
    @Singleton
    fun provideCatalogRepository(
        @ApplicationContext appContext: Context,
        productService: ProductService
    ): CatalogRepository = CatalogRepository(appContext, productService)
}