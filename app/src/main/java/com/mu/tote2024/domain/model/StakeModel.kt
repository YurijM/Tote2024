package com.mu.tote2024.domain.model

data class StakeModel(
    val gameId: String = "",
    val gamblerId: String = "",
    val group: String = "",
    val team1: String = "",
    val team2: String = "",
    val goal1: String = "",
    val goal2: String = "",
    val addGoal1: String = "",
    val addGoal2: String = "",
    val penalty: String = "",
    val points: Double = 0.00,
    val place: Int = 0,
)
