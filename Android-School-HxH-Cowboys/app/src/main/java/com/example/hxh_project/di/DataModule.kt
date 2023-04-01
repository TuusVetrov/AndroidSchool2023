package com.example.hxh_project.di

import com.example.hxh_project.data.repository.CatalogRepository
import com.example.hxh_project.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository = UserRepository()

    @Provides
    @Singleton
    fun provideCatalogRepository(): CatalogRepository = CatalogRepository()
}