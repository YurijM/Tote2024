package com.mu.tote2024.di.module.repository

import com.google.firebase.database.FirebaseDatabase
import com.mu.tote2024.data.repository.TeamRepositoryImpl
import com.mu.tote2024.domain.repository.TeamRepository
import com.mu.tote2024.domain.usecase.team_usecase.GetTeam
import com.mu.tote2024.domain.usecase.team_usecase.GetTeamList
import com.mu.tote2024.domain.usecase.team_usecase.SaveTeam
import com.mu.tote2024.domain.usecase.team_usecase.TeamUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TeamRepositoryModule {
    @Provides
    @Singleton
    fun provideTeamRepository(
        database: FirebaseDatabase
    ) : TeamRepository = TeamRepositoryImpl(database)

    @Provides
    @Singleton
    fun provideTeamUseCase(teamRepository: TeamRepository) = TeamUseCase(
        getTeam = GetTeam(teamRepository),
        getTeamList = GetTeamList(teamRepository),
        saveTeam = SaveTeam(teamRepository)
    )
}