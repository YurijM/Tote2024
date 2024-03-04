package com.mu.tote2024.di.module.repository

import com.google.firebase.database.FirebaseDatabase
import com.mu.tote2024.data.repository.GameRepositoryImpl
import com.mu.tote2024.domain.repository.GameRepository
import com.mu.tote2024.domain.usecase.game_usecase.GameUseCase
import com.mu.tote2024.domain.usecase.game_usecase.GetGame
import com.mu.tote2024.domain.usecase.game_usecase.GetGameList
import com.mu.tote2024.domain.usecase.game_usecase.SaveGame
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GameRepositoryModule {
    @Provides
    @Singleton
    fun provideGameRepository(
        database: FirebaseDatabase
    ) : GameRepository = GameRepositoryImpl(database)

    @Provides
    @Singleton
    fun provideGameUseCase(gameRepository: GameRepository) = GameUseCase(
        getGame = GetGame(gameRepository),
        getGameList = GetGameList(gameRepository),
        saveGame = SaveGame(gameRepository)
    )
}