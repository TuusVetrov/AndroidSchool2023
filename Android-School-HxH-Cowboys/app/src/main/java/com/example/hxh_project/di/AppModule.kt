package com.example.hxh_project.di

import android.app.Application
import com.example.hxh_project.core.token_manager.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideTokenManager(application: Application): TokenManager {
        return TokenManager(TokenManager.tokenPreferences(application))
    }
}