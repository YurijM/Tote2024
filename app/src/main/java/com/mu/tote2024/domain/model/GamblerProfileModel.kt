package com.mu.tote2024.domain.model

import com.mu.tote2024.presentation.utils.Constants

data class GamblerProfileModel(
    val nickname: String = "",
    val photoUrl: String = Constants.EMPTY, // для Picasso поле не должно быть пустым
    val gender: String = ""
)
