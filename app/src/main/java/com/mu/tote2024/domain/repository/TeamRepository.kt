package com.mu.tote2024.domain.repository

import com.mu.tote2024.domain.model.TeamModel
import com.mu.tote2024.presentation.ui.common.UiState
import kotlinx.coroutines.flow.Flow

interface TeamRepository {
    fun getTeam(team: String): Flow<UiState<TeamModel>>
    fun getTeamList(): Flow<UiState<List<TeamModel>>>
    fun saveTeam(team: TeamModel): Flow<UiState<TeamModel>>
}