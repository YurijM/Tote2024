package com.mu.tote2024.domain.model

data class PrognosisModel(
    val gameId: String = "",
    val start: String = "",
    val game: String = "",
    val coefficientForWin: Double = 0.00,
    val coefficientForDraw: Double = 0.00,
    val coefficientForDefeat: Double = 0.00,
    val coefficientForFine: Double = 0.00
)
