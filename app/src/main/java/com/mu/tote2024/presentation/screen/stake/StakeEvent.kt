package com.mu.tote2024.presentation.screen.stake

sealed class StakeEvent {
    data class OnGoalChange(val extraTime: Boolean, val teamNo: Int, val goal: String) : StakeEvent()
    data class OnPenaltyChange(val team: String) : StakeEvent()
    object OnCancel :StakeEvent()
    object OnSave :StakeEvent()
}