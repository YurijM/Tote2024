package com.mu.tote2024.domain.model

data class StakeModel(
    val gameId: String = "",
    var gamblerId: String = "",
    val start: String = "",
    val group: String = "",
    val team1: String = "",
    val team2: String = "",
    var goal1: String = "",
    var goal2: String = "",
    var addGoal1: String = "",
    var addGoal2: String = "",
    var penalty: String = "",
    var points: Double = 0.00,
    var place: Int = 0,
)
