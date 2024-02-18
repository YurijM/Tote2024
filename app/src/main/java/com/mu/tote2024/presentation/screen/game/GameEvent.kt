package com.mu.tote2024.presentation.screen.game

import com.mu.tote2024.domain.model.GameModel

sealed class GameEvent {
    data class OnGameIdChange(val gameId: String) : GameEvent()
    data class OnStartChange(val start: String) : GameEvent()
    data class OnGroupChange(val group: String) : GameEvent()
    data class OnTeamChange(val teamNo: Int, val team: String) : GameEvent()
    data class OnGoalChange(val extraTime: Boolean, val teamNo: Int, val goal: String) : GameEvent()
    data class OnAddGoalChange(val teamNo: Int, val addGoal: Int) : GameEvent()
    data class OnPenaltyChange(val team: String) : GameEvent()
    data class OnSave(val game: GameModel) : GameEvent()
}