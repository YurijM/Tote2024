package com.mu.tote2024.presentation.screen.rating.chart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.domain.usecase.game_usecase.GameUseCase
import com.mu.tote2024.domain.usecase.stake_usecase.StakeUseCase
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.KEY_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RatingChartViewModel @Inject constructor(
    stakeUseCase: StakeUseCase,
    gameUseCase: GameUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state: MutableStateFlow<StateChart> = MutableStateFlow(StateChart())
    val state: StateFlow<StateChart> = _state

    var gamblerId by mutableStateOf(savedStateHandle.get<String>(KEY_ID))
        private set
    val gameIds = mutableListOf<Int>()

    init {
        gamblerId?.let {
            gameUseCase.getGameList().onEach { stateGame ->
                if (stateGame is UiState.Success) {
                    val games = stateGame.data.filter { it.start.toDouble() < System.currentTimeMillis() }.sortedBy { it.gameId }
                        .toMutableList()
                    gameIds.clear()
                    games.forEach { game ->
                        gameIds.add(game.gameId.toInt())
                    }
                }
            }.launchIn(viewModelScope)

            stakeUseCase.getStakeList().onEach { stateStake ->
                _state.value = StateChart(stateStake)
            }.launchIn(viewModelScope)
        }
    }

    companion object {
        data class StateChart(
            val result: UiState<List<StakeModel>> = UiState.Default,
        )
    }
}