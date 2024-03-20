package com.mu.tote2024.domain.model

data class GamblerProfileModel(
    val nickname: String = "",
    val photoUrl: String = "",  //Constants.EMPTY, // для Picasso поле не должно быть пустым
    val gender: String = ""
)
