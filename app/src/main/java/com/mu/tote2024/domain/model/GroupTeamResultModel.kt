package com.mu.tote2024.domain.model

data class GroupTeamResultModel(
    val group: String = "",
    val team: String = "",
    val teamNo: Int = 0,
    val score1: String = "",
    val score2: String = "",
    val score3: String = "",
    val score4: String = "",
    val win: Int = 0,
    val draw: Int = 0,
    val defeat: Int = 0,
    val balls1: Int = 0,
    val balls2: Int = 0,
    val points: Int = 0,
    val place: Int = 0,
)
