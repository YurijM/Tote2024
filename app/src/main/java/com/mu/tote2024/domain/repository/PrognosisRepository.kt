package com.mu.tote2024.domain.repository

import com.mu.tote2024.domain.model.PrognosisModel
import com.mu.tote2024.presentation.ui.common.UiState
import kotlinx.coroutines.flow.Flow

interface PrognosisRepository {
    fun getPrognosis(gameId: String): Flow<UiState<PrognosisModel>>
    fun getPrognosisList(): Flow<UiState<List<PrognosisModel>>>
    fun savePrognosis(prognosis: PrognosisModel): Flow<UiState<Boolean>>
}