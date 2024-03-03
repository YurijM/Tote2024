package com.mu.tote2024.domain.model

import com.mu.tote2024.data.utils.Constants.CURRENT_ID

data class StakeModel(
    var gamblerId: String = CURRENT_ID,
    val gameId: String = "",
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
