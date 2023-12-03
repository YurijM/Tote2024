package com.mu.tote2024.di.module

import com.mu.tote2024.data.repository.AuthRepositoryImpl
import com.mu.tote2024.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providesAuthRepository(repository: AuthRepositoryImpl) : AuthRepository
}