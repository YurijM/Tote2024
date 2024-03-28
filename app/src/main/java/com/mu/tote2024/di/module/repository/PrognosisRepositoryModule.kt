package com.mu.tote2024.di.module.repository

import com.google.firebase.database.FirebaseDatabase
import com.mu.tote2024.data.repository.PrognosisRepositoryImpl
import com.mu.tote2024.domain.repository.PrognosisRepository
import com.mu.tote2024.domain.usecase.prognosis_usecase.GetPrognosis
import com.mu.tote2024.domain.usecase.prognosis_usecase.GetPrognosisList
import com.mu.tote2024.domain.usecase.prognosis_usecase.PrognosisUseCase
import com.mu.tote2024.domain.usecase.prognosis_usecase.SavePrognosis
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrognosisRepositoryModule {
    @Provides
    @Singleton
    fun providePrognosisRepository(
        database: FirebaseDatabase
    ) : PrognosisRepository = PrognosisRepositoryImpl(database)

    @Provides
    @Singleton
    fun providePrognosisUseCase(prognosisRepository: PrognosisRepository) = PrognosisUseCase(
        getPrognosis = GetPrognosis(prognosisRepository),
        getPrognosisList = GetPrognosisList(prognosisRepository),
        savePrognosis = SavePrognosis(prognosisRepository)
    )
}