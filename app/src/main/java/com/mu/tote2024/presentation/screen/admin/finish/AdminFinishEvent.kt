package com.mu.tote2024.presentation.screen.admin.finish

sealed class AdminFinishEvent {
    data class OnIsFinishedChange(val isFinished: Boolean) : AdminFinishEvent()
    data class OnTextChange(val text: String) : AdminFinishEvent()
    object OnCancel : AdminFinishEvent()
    object OnSave : AdminFinishEvent()
}