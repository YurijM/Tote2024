package com.mu.tote2024.di.module.repository

import com.google.firebase.database.FirebaseDatabase
import com.mu.tote2024.data.repository.GamblerRepositoryImpl
import com.mu.tote2024.domain.repository.GamblerRepository
import com.mu.tote2024.domain.usecase.gambler_usecase.GamblerUseCase
import com.mu.tote2024.domain.usecase.gambler_usecase.GetGambler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GamblerRepositoryModule {
    @Provides
    @Singleton
    fun provideGamblerRepository(
        database: FirebaseDatabase
    ) : GamblerRepository = GamblerRepositoryImpl(database)

    @Provides
    @Singleton
    fun provideGamblerUseCase(gamblerRepository: GamblerRepository) = GamblerUseCase(
        getGambler = GetGambler(gamblerRepository)
    )
}