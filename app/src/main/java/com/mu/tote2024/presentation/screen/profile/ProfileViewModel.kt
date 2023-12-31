package com.mu.tote2024.presentation.screen.profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.data.utils.Constants.CURRENT_ID
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_PROFILE_IS_EMPTY
import com.mu.tote2024.data.utils.Constants.GAMBLER
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.domain.model.GamblerProfileModel
import com.mu.tote2024.domain.usecase.auth_usecase.AuthUseCase
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
    authUseCase: AuthUseCase,
    private val gamblerUseCase: GamblerUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    var profileErrors by mutableStateOf(
        ProfileErrors(
            errorNickname = null,
            errorPhotoUrl = null,
            errorGender = null
        )
    )
        private set

    private var photoUri by mutableStateOf<Uri?>(null)

    val sex = listOf("мужской", "женский")

    var profile by mutableStateOf(GamblerProfileModel())
        private set

    init {
        CURRENT_ID = authUseCase.getCurrentUser()

        viewModelScope.launch {
            gamblerUseCase.getGambler(CURRENT_ID).collect {
                if (GamblerState(it).result is UiState.Success) {
                    profile = GamblerProfileModel(
                        nickname = GAMBLER.profile.nickname,
                        gender = GAMBLER.profile.gender,
                        photoUrl = GAMBLER.profile.photoUrl
                    )
                }
            }
        }
    }

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

            is ProfileEvent.OnPhotoChange -> {
                profile = profile.copy(
                    photoUrl = event.photoUri.toString()
                )
                photoUri = event.photoUri

                profileErrors = profileErrors.copy(
                    errorPhotoUrl = checkIsFieldEmpty(event.photoUri.toString())
                )
            }

            is ProfileEvent.OnGenderChange -> {
                profile = profile.copy(
                    gender = event.gender
                )
            }

            is ProfileEvent.OnCancel -> {
                if (checkValues()) {
                    _state.value = ProfileState(UiState.Success(true))
                } else {
                    _state.value = ProfileState(UiState.Error(ERROR_PROFILE_IS_EMPTY))
                }
            }

            is ProfileEvent.OnSave -> {
                if (checkValues()) {
                    viewModelScope.launch {
                        gamblerUseCase.saveProfile(profile).collect {
                            _state.value = ProfileState(it)
                        }
                    }

                    val currentValue = state.value.result
                    if (currentValue is UiState.Success) {
                        viewModelScope.launch {
                            photoUri?.let { uri ->
                                gamblerUseCase.saveGamblerPhoto(CURRENT_ID, uri).collect { state ->
                                    _state.value = ProfileState(state)
                                }
                            }
                        }
                    }

                    val (nickname, photoUrl, gender) = profile
                    GAMBLER = GAMBLER.copy(profile = GamblerProfileModel(nickname, photoUrl, gender))
                }
            }
        }
    }

    private fun checkValues(): Boolean {
        profileErrors = profileErrors.copy(
            errorNickname = checkIsFieldEmpty(profile.nickname)
        )

        profileErrors = profileErrors.copy(
            errorPhotoUrl = checkIsFieldEmpty(profile.photoUrl)
        )

        profileErrors = profileErrors.copy(
            errorGender = checkIsFieldEmpty(profile.gender)
        )

        return (profileErrors.errorNickname != null && profileErrors.errorNickname!!.isBlank()) &&
                (profileErrors.errorPhotoUrl != null && profileErrors.errorPhotoUrl!!.isBlank()) &&
                (profileErrors.errorGender != null && profileErrors.errorGender!!.isBlank())
    }

    companion object {
        data class GamblerState(
            val result: UiState<GamblerModel> = UiState.Default,
        )

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