package com.mu.tote2024.presentation.screen.stake

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.data.utils.Constants.CURRENT_ID
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_GAME_IS_NOT_FOUND
import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.domain.usecase.game_usecase.GameUseCase
import com.mu.tote2024.domain.usecase.stake_usecase.StakeUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.KEY_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StakeViewModel @Inject constructor(
    private val stakeUseCase: StakeUseCase,
    private val gameUseCase: GameUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(StakeState())
    val state: StateFlow<StakeState> = _state

    val gameId by mutableStateOf(savedStateHandle.get<String>(KEY_ID))
    var stake = StakeModel()

    init {
        viewModelScope.launch {
            stakeUseCase.getStake(gameId ?: "", CURRENT_ID).collect { stateStake ->
                when (val result = StakeState(stateStake).result) {
                    is UiState.Success -> {
                        stake = result.data
                        _state.value = StakeState(stateStake)
                    }

                    is UiState.Error -> {
                        if (result.message == ERROR_GAME_IS_NOT_FOUND) {
                            gameUseCase.getGame(gameId ?: "").collect { stateGame ->
                                if (stateGame is UiState.Success) {
                                    val game = stateGame.data
                                    stake = stake.copy(
                                        gameId = game.gameId,
                                        gamblerId = CURRENT_ID,
                                        start = game.start,
                                        group = game.group,
                                        team1 = game.team1,
                                        team2 = game.team2,
                                    )
                                    _state.value = StakeState(UiState.Success(stake))
                                } else {
                                    _state.value = StakeState(UiState.Loading)
                                }
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    companion object {
        data class StakeState(
            val result: UiState<StakeModel> = UiState.Default
        )
    }
}