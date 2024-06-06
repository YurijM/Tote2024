package com.mu.tote2024.presentation.screen.rating.chart

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.model.Point
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.domain.model.GameModel
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

    var gamblers by mutableStateOf(listOf<GamblerModel>())
        private set
    var games by mutableStateOf(listOf<GameModel>())
        private set
    private var movement = mutableListOf<MovementModel>()

    var min = 100F
    var max = 0F

    init {
        gamblerId?.let { id ->
            gamblerUseCase.getGambler(id).onEach { stateGambler ->
                if (stateGambler is UiState.Success) {
                    nickname = stateGambler.data.profile.nickname
                }
            }.launchIn(viewModelScope)
            gamblerUseCase.getGamblerList().onEach { stateGamblers ->
                if (stateGamblers is UiState.Success) {
                    gamblers = stateGamblers.data
                }
            }.launchIn(viewModelScope)
            gameUseCase.getGameList().onEach { stateGame ->
                if (stateGame is UiState.Success) {
                    games = stateGame.data.filter { game -> game.start.toDouble() < System.currentTimeMillis() }
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
                    gamblers.sortedBy { id }.forEach { gambler ->
                        val gamblerStakes = stateStake.data
                            .filter { stake -> stake.gamblerId == gambler.gamblerId && stake.gameId.toInt() in gameIds }

                        movementGambler(gamblerStakes)
                    }

                    gameIds.forEach { gameId ->
                        setPlaceMovementGambler(gameId)
                    }

                    movement.filter { it.gamblerId == gamblerId }.sortedBy { it.gameId }.forEach { item ->
                        places.add(Point(item.gameId.toFloat(), -item.place.toFloat()))
                    }

                    min = minPlace()
                    max = maxPlace()
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun setPlaceMovementGambler(gameId: Int) {
        var place = 0
        var points = 0.0
        movement
            .filter { movement -> movement.gameId == gameId }
            .sortedByDescending { sort -> sort.points }
            .forEach { item ->
                if (item.points != points) {
                    place++
                    points = item.points
                }
                val index = movement.indexOf(
                    movement.find {
                        it.gamblerId == item.gamblerId
                                && it.gameId == gameId
                    }
                )
                movement[index] = item.copy(place = place)
            }
    }

    private fun movementGambler(gamblerStakes: List<StakeModel>) {
        var points = 0.0
        var startPrev = ""
        var indexPrev = 0

        gamblerStakes.sortedBy { it.gameId.toInt() }.forEach { stake ->
            val start = games.find { game -> game.gameId == stake.gameId }?.start

            if (start == startPrev) {
                points = movement[indexPrev].points + stake.points
                movement[indexPrev] = movement[indexPrev].copy(points = points)
            } else {
                points += stake.points
                startPrev = start ?: ""
            }

            movement.add(
                MovementModel(
                    gameId = stake.gameId.toInt(),
                    start = start ?: "",
                    gamblerId = stake.gamblerId,
                    points = points,
                )
            )

            indexPrev = movement.indexOf(
                movement.find {
                    it.gamblerId == stake.gamblerId
                            && it.gameId == stake.gameId.toInt()
                }
            )
        }
    }

    private fun minPlace(): Float {
        var min = (places.size + 1).toFloat()
        places.forEach { place ->
            if (place.y < min) min = place.y
        }
        return min
    }

    private fun maxPlace(): Float {
        var max = -(places.size + 1).toFloat()
        places.forEach { place ->
            if (place.y > max) max = place.y
        }
        return max
    }

    companion object {
        data class StateChart(
            val result: UiState<List<StakeModel>> = UiState.Default,
        )

        data class MovementModel(
            val gameId: Int = 0,
            val start: String = "",
            val gamblerId: String = "",
            val points: Double = 0.0,
            val place: Int = 0,
        )
    }
}