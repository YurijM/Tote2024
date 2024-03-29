package com.mu.tote2024.domain.usecase.gambler_usecase

data class GamblerUseCase(
    val getGambler: GetGambler,
    val saveGambler: SaveGambler,
    val saveProfile: SaveGamblerProfile,
    val saveGamblerPhoto: SaveGamblerPhoto,
    val getGamblerList: GetGamblerList,
    val getEmail: GetEmail,
    val getEmailList: GetEmailList,
    val saveEmail: SaveEmail,
    val deleteEmail: DeleteEmail
)
