package com.mu.tote2024.presentation.screen.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.model.GamblerProfileModel
import com.mu.tote2024.domain.usecase.gambler_usecase.GamblerUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.checkIsFieldEmpty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val gamblerUseCase: GamblerUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    var enabledButton by mutableStateOf(false)
        private set

    var profile by mutableStateOf(GamblerProfileModel())
        private set

    var profileErrors by mutableStateOf(
        ProfileErrors(
            errorNickname = null,
            errorPhotoUrl = null,
            errorGender = null
        )
    )
        private set

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnNicknameChange -> {
                profile = profile.copy(
                    nickname = event.nickname
                )
                profileErrors = profileErrors.copy(
                    errorNickname = checkIsFieldEmpty(event.nickname)
                )
            }
            is ProfileEvent.OnPhotoChange -> {}
            is ProfileEvent.OnGenderChange -> {}
            is ProfileEvent.OnSave -> {
                viewModelScope.launch {
                    gamblerUseCase.saveProfile(profile)
                }
            }
        }
    }

    private fun checkValues(): Boolean {
        return (profileErrors.errorNickname != null && profileErrors.errorNickname!!.isBlank()) &&
                (profileErrors.errorPhotoUrl != null && profileErrors.errorPhotoUrl!!.isBlank()) &&
                (profileErrors.errorGender != null && profileErrors.errorGender!!.isBlank())
    }

    companion object {
        data class ProfileState(
            val result: UiState<Boolean> = UiState.Default,
        )

        data class ProfileErrors(
            val errorNickname: String?,
            val errorPhotoUrl: String?,
            val errorGender: String?,
        )
    }
}