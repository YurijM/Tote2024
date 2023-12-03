package com.mu.tote2024.presentation.ui.common

sealed class UiEvent {
    object PopBackStack : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    data class ShowSnackBar(val message: String) : UiEvent()
}
