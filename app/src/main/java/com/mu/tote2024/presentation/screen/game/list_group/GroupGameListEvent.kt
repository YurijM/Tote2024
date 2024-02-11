package com.mu.tote2024.presentation.screen.game.list_group

import com.mu.tote2024.domain.model.GameModel

sealed class GroupGameListEvent {
    data class OnGoalChange(val teamNo: Int, val goal: String) : GroupGameListEvent()
    data class OnSave(val game: GameModel) : GroupGameListEvent()
}