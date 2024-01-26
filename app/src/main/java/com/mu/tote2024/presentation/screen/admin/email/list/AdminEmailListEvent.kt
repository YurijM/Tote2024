package com.mu.tote2024.presentation.screen.admin.email.list

sealed class AdminEmailListEvent {
    data class OnDelete(val id: String) : AdminEmailListEvent()
}