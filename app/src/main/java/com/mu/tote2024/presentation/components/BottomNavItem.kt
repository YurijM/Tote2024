package com.mu.tote2024.presentation.components

import com.mu.tote2024.R
import com.mu.tote2024.presentation.utils.Constants

sealed class BottomNavItem(
    val titleId: Int,
    val iconId: Int,
    val route: String
) {
    object RatingItem: BottomNavItem(
        titleId = R.string.rating,
        iconId = R.drawable.ic_rating,
        route = Constants.Routes.RATING_SCREEN
    )
    object StakeItem: BottomNavItem(
        titleId = R.string.stakes,
        iconId = R.drawable.ic_ruble,
        route = Constants.Routes.STAKE_LIST_SCREEN
    )
    object PrognosisItem: BottomNavItem(
        titleId = R.string.prognosis,
        iconId = R.drawable.ic_prognosis,
        route = Constants.Routes.PROGNOSIS_SCREEN
    )
    object GamesItem: BottomNavItem(
        titleId = R.string.games,
        iconId = R.drawable.ic_games,
        route = Constants.Routes.GAME_LIST_SCREEN
    )
}
