package com.mu.tote2024.presentation.screen.profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.data.utils.Constants.CURRENT_ID
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_CANCEL_WHEN_PROFILE_IS_EMPTY
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_PROFILE_IS_EMPTY
import com.mu.tote2024.domain.model.GamblerModel
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
    private val _state: MutableStateFlow<GamblerState> = MutableStateFlow(GamblerState())
    val state: StateFlow<GamblerState> = _state.asStateFlow()

    private val _stateExit: MutableStateFlow<ExitState> = MutableStateFlow(ExitState())
    val stateExit: StateFlow<ExitState> = _stateExit

    var profileErrors by mutableStateOf(
        ProfileErrors(
            errorNickname = null,
            errorPhotoUrl = null,
            errorGender = null
        )
    )
        private set

    var enabled by mutableStateOf(false)
        private set

    private var photoUri by mutableStateOf<Uri?>(null)

    var gambler by mutableStateOf(GamblerModel())
        private set

    init {
        CURRENT_ID = authUseCase.getCurrentUserId()

        viewModelScope.launch {
            gamblerUseCase.getGambler(CURRENT_ID).collect { stateGambler ->
                val result = GamblerState(stateGambler).result

                if (result is UiState.Success) {
                    gambler = result.data
                    enabled = checkValues()
                }

                _state.value = GamblerState(stateGambler)
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnNicknameChange -> {
                gambler = gambler.copy(
                    profile = gambler.profile.copy(nickname = event.nickname)
                )
                enabled = checkValues()
            }

            is ProfileEvent.OnPhotoChange -> {
                gambler = gambler.copy(
                    profile = gambler.profile.copy(photoUrl = event.photoUri.toString())
                )
                photoUri = event.photoUri
                enabled = checkValues()
            }

            is ProfileEvent.OnGenderChange -> {
                gambler = gambler.copy(
                    profile = gambler.profile.copy(gender = event.gender)
                )
                enabled = checkValues()
            }

            is ProfileEvent.OnCancel -> {
                if (checkValues()) {
                    _stateExit.value = ExitState(UiState.Success(true))
                } else {
                    _stateExit.value = ExitState(UiState.Error(ERROR_CANCEL_WHEN_PROFILE_IS_EMPTY))
                }
            }

            is ProfileEvent.OnSave -> {
                if (checkValues()) {
                    viewModelScope.launch {
                        gamblerUseCase.saveGambler(gambler).collect {
                            _state.value = GamblerState(it)
                        }
                    }

                    val currentValue = _state.value.result
                    if (currentValue is UiState.Success) {
                        viewModelScope.launch {
                            photoUri?.let { uri ->
                                gamblerUseCase.saveGamblerPhoto(CURRENT_ID, uri).collect { state ->
                                    _stateExit.value = ExitState(state)
                                }
                            }
                        }
                    }

                    //val (nickname, photoUrl, gender) = profile
                    //GAMBLER = GAMBLER.copy(profile = GamblerProfileModel(nickname, photoUrl, gender))
                } else {
                    _stateExit.value = ExitState(UiState.Error(ERROR_PROFILE_IS_EMPTY))
                }
            }
        }
    }

    private fun checkValues(): Boolean {
        profileErrors = profileErrors.copy(
            errorNickname = checkIsFieldEmpty(gambler.profile.nickname)
        )

        profileErrors = profileErrors.copy(
            errorPhotoUrl = checkIsFieldEmpty(gambler.profile.photoUrl)
        )

        profileErrors = profileErrors.copy(
            errorGender = checkIsFieldEmpty(gambler.profile.gender)
        )

        return profileErrors.errorNickname.isNullOrBlank() &&
                profileErrors.errorPhotoUrl.isNullOrBlank() &&
                profileErrors.errorGender.isNullOrBlank()
    }

    companion object {
        data class GamblerState(
            val result: UiState<GamblerModel> = UiState.Default,
        )

        data class ExitState(
            val result: UiState<Boolean> = UiState.Default,
        )

        data class ProfileErrors(
            val errorNickname: String?,
            val errorPhotoUrl: String?,
            val errorGender: String?,
        )
    }
}