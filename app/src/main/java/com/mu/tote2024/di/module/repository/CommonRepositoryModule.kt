package com.mu.tote2024.di.module.repository

import com.mu.tote2024.data.repository.CommonRepositoryImpl
import com.mu.tote2024.domain.repository.CommonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonRepositoryModule {
    @Provides
    @Singleton
    fun provideCommonRepository() : CommonRepository = CommonRepositoryImpl()
}