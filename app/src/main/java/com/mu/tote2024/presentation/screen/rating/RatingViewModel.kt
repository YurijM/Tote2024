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
import kotlin.math.round

@HiltViewModel
class RatingViewModel @Inject constructor(
    gamblerUseCase: GamblerUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<RatingState> = MutableStateFlow(RatingState())
    val state: StateFlow<RatingState> = _state.asStateFlow()

    var showArrows = false
        private set
    lateinit var winningsList: MutableList<WinningsModel>
        private set
    lateinit var winners: List<GamblerModel>
        private set

    init {
        viewModelScope.launch {
            gamblerUseCase.getGamblerList().collect { stateGambler ->
                winningsList = mutableListOf()
                winners = mutableListOf()
                if (stateGambler is UiState.Success) {
                    val gamblers = stateGambler.data

                    showArrows = gamblers.maxOf { it.result.placePrev } > 0

                    val gamblersCount = gamblers.size
                    winners = gamblers
                        .filter { it.result.place in 1..3 }
                        .sortedWith(
                            compareBy<GamblerModel> { it.result.place }
                                .thenBy { it.profile.nickname }
                        )
                    val sumRates = gamblers.sumOf { it.rate }.toDouble()
                    val averageRate = sumRates / gamblersCount

                    var winningsSecondPart = sumRates

                    winners.forEach { winner ->
                        val firstPartCoefficient = when (winner.result.place) {
                            1 -> 3
                            2 -> 6
                            3 -> 12
                            else -> 0
                        }

                        winningsSecondPart -= (gamblersCount / firstPartCoefficient) * winner.rate
                    }

                    val winnerAttrs = getWinnerAttrs(winners)

                    var attr = winnerAttrs.find { item -> item.place == 1 }
                    if (attr != null) winnerAttrs[0] = attr.copy(addCoefficient = calcAddCoefficient(attr, winnerAttrs))

                    attr = winnerAttrs.find { item -> item.place == 2 }
                    if (attr != null) winnerAttrs[1] = attr.copy(addCoefficient = calcAddCoefficient(attr, winnerAttrs))

                    var sumCoefficients = 0.0

                    winners.forEach { winner ->
                        sumCoefficients += (winner.rate / averageRate) *
                                (winnerAttrs.find { item -> item.place == winner.result.place }?.addCoefficient ?: 1.0)
                    }

                    val sumForCalc = winningsSecondPart / sumCoefficients

                    winners.forEach { winner ->
                        val firstPartCoefficient = when (winner.result.place) {
                            1 -> 3
                            2 -> 6
                            3 -> 12
                            else -> 0
                        }
                        val winningsFirstPart = (gamblersCount / firstPartCoefficient) * winner.rate
                        val rateCoefficient = winner.rate.toDouble() / averageRate
                        val coefficient = rateCoefficient * (winnerAttrs.find { item -> item.place == winner.result.place }?.addCoefficient ?: 1.0)

                        winningsList.add(
                            WinningsModel(
                                gamblerId = winner.gamblerId ?: "",
                                winnings = (round(winningsFirstPart + coefficient * sumForCalc)).toInt()
                            )
                        )
                    }
                }

                _state.value = RatingState(stateGambler)
            }
        }
    }

    fun checkProfile(): Boolean {
        return GAMBLER.profile.nickname.isNotBlank()
                && GAMBLER.profile.gender.isNotBlank()
                && GAMBLER.profile.photoUrl.isNotBlank()
    }

    private fun getWinnerAttrs(winners: List<GamblerModel>): MutableList<WinnerAttr> =
        mutableListOf(
            WinnerAttr(
                place = 1,
                points = winners.find { item -> item.result.place == 1 }?.result?.points ?: 0.0,
                gamblersCount = winners.filter { item -> item.result.place == 1 }.size,
            ),
            WinnerAttr(
                place = 2,
                points = winners.find { item -> item.result.place == 2 }?.result?.points ?: 0.0,
                gamblersCount = winners.filter { item -> item.result.place == 2 }.size,
            ),
            WinnerAttr(
                place = 3,
                points = winners.find { item -> item.result.place == 3 }?.result?.points ?: 0.0,
                gamblersCount = winners.filter { item -> item.result.place == 3 }.size,
            ),
        )

    private fun calcAddCoefficient(attr: WinnerAttr, attrs: List<WinnerAttr>): Double =
        if (attr.place == 1) {
            if (attr.gamblersCount <= 2) {
                (attr.points - (attrs.find { item -> item.place == 3 }?.points
                    ?: attrs.find { item -> item.place == 2 }?.points ?: 0.0)) / 10 + 1
            } else 1.0
        } else if (attr.place == 2) {
            if (attr.gamblersCount == 1) {
                (attr.points - (attrs.find { item -> item.place == 3 }?.points ?: 0.0)) / 10 + 1
            } else 1.0
        } else 1.0


    companion object {
        data class RatingState(
            val result: UiState<List<GamblerModel>> = UiState.Default,
        )

        data class WinningsModel(
            val gamblerId: String,
            val winnings: Int
        )

        data class WinnerAttr(
            val place: Int = 0,
            val points: Double = 0.0,
            val gamblersCount: Int = 0,
            val addCoefficient: Double = 1.0
        )
    }
}