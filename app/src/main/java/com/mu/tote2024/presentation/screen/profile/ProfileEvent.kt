package com.mu.tote2024.presentation.screen.profile

import com.mu.tote2024.domain.model.GamblerProfileModel

sealed class ProfileEvent {
    data class OnNicknameChange(val nickname: String) : ProfileEvent()
    data class OnPhotoChange(val photoUrl: String) : ProfileEvent()
    data class OnGenderChange(val gender: String) : ProfileEvent()
    data class OnSave(val profile: GamblerProfileModel) : ProfileEvent()
}