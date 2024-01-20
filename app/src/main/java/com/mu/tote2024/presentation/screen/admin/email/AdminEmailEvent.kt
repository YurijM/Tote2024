package com.mu.tote2024.presentation.screen.admin.email

sealed class AdminEmailEvent {
    data class OnEmailChange(val email: String) : AdminEmailEvent()
    object OnCancel : AdminEmailEvent()
    object OnSave : AdminEmailEvent()
}