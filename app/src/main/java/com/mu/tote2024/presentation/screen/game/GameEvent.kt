package com.mu.tote2024.presentation.screen.game

sealed class GameEvent {
    data class OnGameIdChange(val gameId: String) : GameEvent()
    data class OnStartChange(val start: String) : GameEvent()
    data class OnGroupChange(val group: String) : GameEvent()
    data class OnTeamChange(val teamNo: Int, val team: String) : GameEvent()
    data class OnGoalChange(val extraTime: Boolean, val teamNo: Int, val goal: String) : GameEvent()
    data class OnPenaltyChange(val team: String) : GameEvent()
    object OnCancel : GameEvent()
    object OnSave : GameEvent()
    object OnGenerateResult : GameEvent()
}