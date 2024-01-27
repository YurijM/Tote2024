package com.mu.tote2024.domain.usecase.game_usecase

import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.repository.GameRepository

class SaveGame(
    private val gameRepository: GameRepository
) {
    operator fun invoke(game: GameModel) = gameRepository.saveGame(game)
}