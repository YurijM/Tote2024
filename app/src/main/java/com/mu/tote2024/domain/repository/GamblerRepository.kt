package com.mu.tote2024.domain.repository

import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.domain.model.GamblerProfileModel
import com.mu.tote2024.domain.model.GamblerResultModel
import com.mu.tote2024.presentation.ui.common.UiState
import kotlinx.coroutines.flow.Flow

interface GamblerRepository {
    fun saveGambler(gambler: GamblerModel): Flow<UiState<GamblerModel>>
    fun saveGamblerProfile(profile: GamblerProfileModel): Flow<UiState<GamblerProfileModel>>
    fun saveGamblerResult(result: GamblerResultModel): Flow<UiState<GamblerResultModel>>
    fun getGambler(gamblerId: String): Flow<UiState<GamblerModel>>
}