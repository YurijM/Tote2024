package com.mu.tote2024.presentation.screen.game

import com.mu.tote2024.domain.model.GameModel

sealed class GameEvent {
    data class OnGoalChange(val teamNo: Int, val goal: String) : GameEvent()
    data class OnSave(val game: GameModel) : GameEvent()
}