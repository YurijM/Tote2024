package com.mu.tote2024.presentation.screen.rating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.data.utils.Constants.GAMBLER
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.domain.usecase.gambler_usecase.GamblerUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatingViewModel @Inject constructor(
    gamblerUseCase: GamblerUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<RatingState> = MutableStateFlow(RatingState())
    val state: StateFlow<RatingState> = _state.asStateFlow()

    var showArrow = false
        private set

    init {
        viewModelScope.launch {
            gamblerUseCase.getGamblerList().collect { stateGambler ->
                if (stateGambler is UiState.Success) {
                    showArrow = stateGambler.data.maxOf { it.result.placePrev } > 0
                }
                _state.value = RatingState(stateGambler)
            }
        }
    }

    fun checkProfile() : Boolean {
        return GAMBLER.profile.nickname.isNotBlank()
                && GAMBLER.profile.gender.isNotBlank()
                && GAMBLER.profile.photoUrl.isNotBlank()
    }

    companion object {
        data class RatingState(
            val result: UiState<List<GamblerModel>> = UiState.Default,
        )
    }
}