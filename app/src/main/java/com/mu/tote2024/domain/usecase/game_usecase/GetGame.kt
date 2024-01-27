package com.mu.tote2024.domain.usecase.game_usecase

import com.mu.tote2024.domain.repository.GameRepository

class GetGame(
    private val gameRepository: GameRepository
) {
    operator fun invoke(id: String) = gameRepository.getGame(id)
}