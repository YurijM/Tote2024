package com.mu.tote2024.domain.repository

import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.presentation.ui.common.UiState
import kotlinx.coroutines.flow.Flow

interface StakeRepository {
    fun getStake(gameId: String, gamblerId: String): Flow<UiState<StakeModel>>
    fun getStakeList(): Flow<UiState<List<StakeModel>>>
    fun saveStake(stake: StakeModel): Flow<UiState<Boolean>>
}