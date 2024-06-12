package com.mu.tote2024.presentation.screen.admin.finish

sealed class AdminFinishEvent {
    data class OnFinishChange(val finish: Boolean) : AdminFinishEvent()
    data class OnTextChange(val text: String) : AdminFinishEvent()
    object OnCancel : AdminFinishEvent()
    object OnSave : AdminFinishEvent()
}