package com.mu.tote2024.presentation.screen.admin.email

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.model.EmailModel
import com.mu.tote2024.domain.usecase.gambler_usecase.GamblerUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.ID_NEW_EMAIL
import com.mu.tote2024.presentation.utils.Constants.KEY_ID
import com.mu.tote2024.presentation.utils.checkEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminEmailViewModel @Inject constructor(
    private val gamblerUseCase: GamblerUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state: MutableStateFlow<AdminEmailState> = MutableStateFlow(AdminEmailState())
    val state: StateFlow<AdminEmailState> = _state.asStateFlow()

    var email by mutableStateOf(EmailModel())
        private set

    var errorEmail by mutableStateOf(checkEmail(email.email))
        private set

    var isExit by mutableStateOf(false)
        private set

    init {
        val emailId = savedStateHandle.get<String>(KEY_ID)
        if (emailId != null) {
            if (emailId != ID_NEW_EMAIL) {
                viewModelScope.launch {
                    gamblerUseCase.getEmail(emailId).collect { state ->
                        _state.value = AdminEmailState(state)

                        if (state is UiState.Success) {
                            email = state.data
                            errorEmail = checkEmail(email.email)
                        }
                    }
                }
            } else {
                viewModelScope.launch {
                    gamblerUseCase.getEmailList().collect { state ->
                        if (state is UiState.Success) {
                            //email = email.copy(emailId = (state.data.size + 1).toString())
                            val maxEmailId = state.data.maxByOrNull { email -> email.emailId.toInt() }
                            email = if (maxEmailId != null) {
                                email.copy(emailId = (maxEmailId.emailId.toInt() + 1).toString())
                            } else {
                                email.copy(emailId = "1")
                            }
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: AdminEmailEvent) {
        when (event) {
            is AdminEmailEvent.OnEmailChange -> {
                errorEmail = checkEmail(event.email)

                email = email.copy(
                    email = event.email
                )
            }

            is AdminEmailEvent.OnCancel -> {
                _state.value = AdminEmailState(UiState.Success(email))
                isExit = true
            }

            is AdminEmailEvent.OnSave -> {
                if (errorEmail.isBlank()) {
                    viewModelScope.launch {
                        gamblerUseCase.saveEmail(email).collect { state ->
                            if (state is UiState.Success) {
                                _state.value = AdminEmailState(state)
                                isExit = true
                            }
                        }
                    }
                } else {
                    _state.value = AdminEmailState(UiState.Error(errorEmail))
                }
            }
        }
    }

    companion object {
        data class AdminEmailState(
            val result: UiState<EmailModel> = UiState.Default,
        )
    }
}