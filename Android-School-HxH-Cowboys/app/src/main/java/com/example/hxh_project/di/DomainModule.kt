package com.example.hxh_project.di

import android.content.Context
import com.example.hxh_project.data.repository.UserRepository
import com.example.hxh_project.domain.model.Profile
import com.example.hxh_project.domain.use_case.ProfileUseCase
import com.example.hxh_project.domain.use_case.SignInUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {
    @Provides
    fun provideSignInUseCase(userRepository: UserRepository): SignInUseCase =
        SignInUseCase(userRepository)

    @Provides
    fun provideProfileUseCase(userRepository: UserRepository,
                              @ApplicationContext context: Context): ProfileUseCase =
        ProfileUseCase(userRepository, context)
}