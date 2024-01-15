package com.mu.tote2024.presentation.screen.admin.gambler.list

import com.mu.tote2024.domain.model.GamblerModel

sealed class AdminGamblerListEvent {
    data class OnClickItem(val gambler: GamblerModel) : AdminGamblerListEvent()
}