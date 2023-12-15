package com.mu.tote2024.presentation.screen.profile

sealed class ProfileEvent {
    data class OnNicknameChange(val nickname: String) : ProfileEvent()
    data class OnPhotoChange(val photoUrl: String) : ProfileEvent()
    data class OnGenderChange(val gender: String) : ProfileEvent()
    object OnSave : ProfileEvent()
}