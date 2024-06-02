package com.mu.tote2024.presentation.screen.rating.chart

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.model.Point
import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.domain.usecase.gambler_usecase.GamblerUseCase
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

@SuppressLint("MutableCollectionMutableState")
@HiltViewModel
class RatingChartViewModel @Inject constructor(
    stakeUseCase: StakeUseCase,
    gameUseCase: GameUseCase,
    gamblerUseCase: GamblerUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state: MutableStateFlow<StateChart> = MutableStateFlow(StateChart())
    val state: StateFlow<StateChart> = _state

    var gamblerId by mutableStateOf(savedStateHandle.get<String>(KEY_ID))
        private set
    val gameIds = mutableListOf<Int>()
    var nickname by mutableStateOf("")
    val places by mutableStateOf<ArrayList<Point>>(arrayListOf())

    var min = 100F
    var max = 0F

    init {
        gamblerId?.let {
            gamblerUseCase.getGambler(it).onEach { stateGambler ->
                if (stateGambler is UiState.Success) {
                    nickname = stateGambler.data.profile.nickname
                }
            }.launchIn(viewModelScope)
            gameUseCase.getGameList().onEach { stateGame ->
                if (stateGame is UiState.Success) {
                    val games = stateGame.data.filter { game -> game.start.toDouble() < System.currentTimeMillis() }
                        .sortedBy { game -> game.gameId.toInt() }
                        .toMutableList()
                    gameIds.clear()
                    games.forEach { game ->
                        gameIds.add(game.gameId.toInt())
                    }
                }
            }.launchIn(viewModelScope)
            stakeUseCase.getStakeList().onEach { stateStake ->
                _state.value = StateChart(stateStake)
                if (stateStake is UiState.Success) {
                    stateStake.data.filter { stake -> stake.gamblerId == gamblerId && stake.gameId.toInt() in gameIds }
                    .sortedBy { stake -> stake.gameId.toInt() }.forEach { stake ->
                        places.add(Point(stake.gameId.toFloat(), stake.place.toFloat()))
                    }

                    min = minPlace()
                    max = maxPlace()
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun minPlace(): Float {
        var min = 100F
        places.forEach { place ->
            if (place.y < min) min = place.y
        }
        return min
    }

    private fun maxPlace(): Float {
        var max = 0F
        places.forEach { place ->
            if (place.y > max) max = place.y
        }
        return max
    }

    companion object {
        data class StateChart(
            val result: UiState<List<StakeModel>> = UiState.Default,
        )
    }
}