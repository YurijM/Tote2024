package com.mu.tote2024.presentation.screen.profile

import android.net.Uri

sealed class ProfileEvent {
    data class OnNicknameChange(val nickname: String) : ProfileEvent()
    data class OnPhotoChange(val photoUri: Uri) : ProfileEvent()
    data class OnGenderChange(val gender: String) : ProfileEvent()
    object OnCancel : ProfileEvent()
    object OnSave : ProfileEvent()
}