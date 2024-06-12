package com.mu.tote2024.domain.repository

import com.mu.tote2024.domain.model.FinishModel
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.presentation.ui.common.UiState
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getGame(id: String): Flow<UiState<GameModel>>
    fun getGameList(): Flow<UiState<List<GameModel>>>
    fun saveGame(game: GameModel): Flow<UiState<Boolean>>
    fun getFinish(): Flow<UiState<FinishModel>>
    fun saveFinish(finish: FinishModel): Flow<UiState<FinishModel>>
}