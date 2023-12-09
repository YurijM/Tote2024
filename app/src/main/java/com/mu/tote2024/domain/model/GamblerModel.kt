package com.mu.tote2024.domain.model

data class GamblerModel(
    val gamblerId: String? = null,
    val email: String = "",
    val rate: Int = 0,
    val admin: Boolean = false,
    val profile: GamblerProfileModel? = null,
    val result: GamblerResultModel? = null
)
