package com.mu.tote2024.presentation.screen.admin.email.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.model.EmailModel
import com.mu.tote2024.domain.usecase.gambler_usecase.GamblerUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminEmailListViewModel @Inject constructor(
    private val gamblerUseCase: GamblerUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<AdminEmailListState> = MutableStateFlow(AdminEmailListState())
    val state: StateFlow<AdminEmailListState> = _state

    init {
        viewModelScope.launch {
            gamblerUseCase.getEmailList().collect {
                _state.value = AdminEmailListState(it)
            }
        }
    }

    companion object {
        data class AdminEmailListState(
            val result: UiState<List<EmailModel>> = UiState.Default
        )
    }
}