package com.mu.tote2024.di.module.repository

import com.google.firebase.database.FirebaseDatabase
import com.mu.tote2024.data.repository.StakeRepositoryImpl
import com.mu.tote2024.domain.repository.StakeRepository
import com.mu.tote2024.domain.usecase.stake_usecase.StakeUseCase
import com.mu.tote2024.domain.usecase.stake_usecase.GetStake
import com.mu.tote2024.domain.usecase.stake_usecase.GetStakeList
import com.mu.tote2024.domain.usecase.stake_usecase.SaveStake
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StakeRepositoryModule {
    @Provides
    @Singleton
    fun provideStakeRepository(
        database: FirebaseDatabase
    ) : StakeRepository = StakeRepositoryImpl(database)

    @Provides
    @Singleton
    fun provideStakeUseCase(stakeRepository: StakeRepository) = StakeUseCase(
        getStake = GetStake(stakeRepository),
        getStakeList = GetStakeList(stakeRepository),
        saveStake = SaveStake(stakeRepository)
    )
}