package com.mu.tote2024.di.module.repository

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.mu.tote2024.data.repository.GamblerRepositoryImpl
import com.mu.tote2024.domain.repository.GamblerRepository
import com.mu.tote2024.domain.usecase.gambler_usecase.DeleteEmail
import com.mu.tote2024.domain.usecase.gambler_usecase.GamblerUseCase
import com.mu.tote2024.domain.usecase.gambler_usecase.GetEmail
import com.mu.tote2024.domain.usecase.gambler_usecase.GetEmailList
import com.mu.tote2024.domain.usecase.gambler_usecase.GetGambler
import com.mu.tote2024.domain.usecase.gambler_usecase.GetGamblerList
import com.mu.tote2024.domain.usecase.gambler_usecase.SaveEmail
import com.mu.tote2024.domain.usecase.gambler_usecase.SaveGambler
import com.mu.tote2024.domain.usecase.gambler_usecase.SaveGamblerPhoto
import com.mu.tote2024.domain.usecase.gambler_usecase.SaveGamblerProfile
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
        database: FirebaseDatabase,
        storage: FirebaseStorage
    ): GamblerRepository = GamblerRepositoryImpl(database, storage)

    @Provides
    @Singleton
    fun provideGamblerUseCase(gamblerRepository: GamblerRepository) = GamblerUseCase(
        getGambler = GetGambler(gamblerRepository),
        saveGambler = SaveGambler(gamblerRepository),
        saveProfile = SaveGamblerProfile(gamblerRepository),
        saveGamblerPhoto = SaveGamblerPhoto(gamblerRepository),
        getGamblerList = GetGamblerList(gamblerRepository),
        getEmail = GetEmail(gamblerRepository),
        getEmailList = GetEmailList(gamblerRepository),
        saveEmail = SaveEmail(gamblerRepository),
        deleteEmail = DeleteEmail(gamblerRepository)
    )
}