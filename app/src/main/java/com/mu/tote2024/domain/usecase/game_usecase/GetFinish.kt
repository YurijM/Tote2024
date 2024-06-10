package com.mu.tote2024.domain.usecase.game_usecase

import com.mu.tote2024.domain.repository.GameRepository

class GetFinish(
    private val gameRepository: GameRepository
) {
    operator fun invoke() = gameRepository.getFinish()
}