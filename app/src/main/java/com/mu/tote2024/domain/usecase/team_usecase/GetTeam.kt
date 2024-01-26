package com.mu.tote2024.domain.usecase.team_usecase

import com.mu.tote2024.domain.repository.TeamRepository

class GetTeam(
    private val teamRepository: TeamRepository
) {
    operator fun invoke(team: String) =
        teamRepository.getTeam(team)
}