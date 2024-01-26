package com.mu.tote2024.domain.usecase.team_usecase

import com.mu.tote2024.domain.model.TeamModel
import com.mu.tote2024.domain.repository.TeamRepository

class SaveTeam(
    private val teamRepository: TeamRepository
) {
    operator fun invoke(team: TeamModel) =
        teamRepository.saveTeam(team)
}