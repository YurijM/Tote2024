package com.mu.tote2024.presentation.screen.admin.profile

sealed class AdminProfileEvent {
    data class OnRateChange(val rate: String) : AdminProfileEvent()
    data class OnIsAdminChange(val isAdmin: Boolean) : AdminProfileEvent()
    object OnCancel : AdminProfileEvent()
    object OnSave : AdminProfileEvent()
}