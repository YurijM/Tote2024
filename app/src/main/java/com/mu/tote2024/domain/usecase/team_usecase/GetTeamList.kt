package com.mu.tote2024.domain.usecase.team_usecase

import com.mu.tote2024.domain.repository.TeamRepository

class GetTeamList(
    private val teamRepository: TeamRepository
) {
    operator fun invoke() =
        teamRepository.getTeamList()
}