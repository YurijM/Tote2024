package com.mu.tote2024.domain.usecase.game_usecase

import com.mu.tote2024.domain.model.FinishModel
import com.mu.tote2024.domain.repository.GameRepository

class SaveFinish(
    private val gameRepository: GameRepository
) {
    operator fun invoke(finish: FinishModel) = gameRepository.saveFinish(finish)
}