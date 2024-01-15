package com.mu.tote2024.presentation.screen.admin.gambler

sealed class AdminGamblerEvent {
    data class OnRateChange(val rate: String) : AdminGamblerEvent()
    data class OnIsAdminChange(val isAdmin: Boolean) : AdminGamblerEvent()
    object OnCancel : AdminGamblerEvent()
    object OnSave : AdminGamblerEvent()
}