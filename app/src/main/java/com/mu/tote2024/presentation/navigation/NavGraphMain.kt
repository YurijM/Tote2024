package com.mu.tote2024.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.mu.tote2024.presentation.navigation.destination.admin.adminEmail
import com.mu.tote2024.presentation.navigation.destination.admin.adminEmailList
import com.mu.tote2024.presentation.navigation.destination.admin.adminGambler
import com.mu.tote2024.presentation.navigation.destination.admin.adminGamblerList
import com.mu.tote2024.presentation.navigation.destination.admin.adminGamblerPhoto
import com.mu.tote2024.presentation.navigation.destination.admin.adminGameList
import com.mu.tote2024.presentation.navigation.destination.admin.adminMain
import com.mu.tote2024.presentation.navigation.destination.admin.adminStakeList
import com.mu.tote2024.presentation.navigation.destination.admin.adminTeamList
import com.mu.tote2024.presentation.navigation.destination.admin.navigateToAdminEmail
import com.mu.tote2024.presentation.navigation.destination.admin.navigateToAdminEmailList
import com.mu.tote2024.presentation.navigation.destination.admin.navigateToAdminGambler
import com.mu.tote2024.presentation.navigation.destination.admin.navigateToAdminGamblerPhoto
import com.mu.tote2024.presentation.navigation.destination.game.game
import com.mu.tote2024.presentation.navigation.destination.game.gameList
import com.mu.tote2024.presentation.navigation.destination.game.groupGameList
import com.mu.tote2024.presentation.navigation.destination.game.navigateToGame
import com.mu.tote2024.presentation.navigation.destination.game.navigateToGroupGameList
import com.mu.tote2024.presentation.navigation.destination.prognosis.prognosis
import com.mu.tote2024.presentation.navigation.destination.rating.rating
import com.mu.tote2024.presentation.navigation.destination.stake.navigateToStake
import com.mu.tote2024.presentation.navigation.destination.stake.navigateToStakeList
import com.mu.tote2024.presentation.navigation.destination.stake.stake
import com.mu.tote2024.presentation.navigation.destination.stake.stakeList
import com.mu.tote2024.presentation.utils.Constants.Routes.RATING_SCREEN

@Composable
fun NavGraphMain(
    navMainController: NavHostController
) {
    NavHost(
        navController = navMainController,
        startDestination = RATING_SCREEN
    ) {
        rating(
            toAdminGamblerPhoto = { photoUrl ->
                navMainController.navigateToAdminGamblerPhoto(photoUrl)
            }
        )

        stakeList(
            toStake = { gameId, gamblerId ->
                navMainController.navigateToStake(gameId, gamblerId)
            }
        )

        stake(
            toStakeList = {
                //navMainController.popBackStack()
                navMainController.navigateToStakeList()
            }
        )

        prognosis()

        gameList(
            toGroupGameList = { group ->
                navMainController.navigateToGroupGameList(group)
            },
            toGame = { id ->
                navMainController.navigateToGame(id)
            }
        )

        groupGameList(
            toGame = { id ->
                navMainController.navigateToGame(id)
            }
        )

        game(
            toGameList = {
                navMainController.popBackStack()
            },
        )

        adminMain(
            toItem = { route ->
                navMainController.navigate(route)
            }
        )

        adminEmailList(
            toEmail = { id ->
                navMainController.navigateToAdminEmail(id)
            }
        )

        adminEmail(
            toAdminEmailList = {
                navMainController.navigateToAdminEmailList()
            }
        )

        adminGamblerList(
            toGambler = { id ->
                navMainController.navigateToAdminGambler(id)
            }
        )

        adminGambler(
            toAdminGamblerList = {
                navMainController.popBackStack()
            },
            toAdminGamblerPhoto = { photoUrl ->
                navMainController.navigateToAdminGamblerPhoto(photoUrl)
            }
        )

        adminGamblerPhoto()

        adminTeamList()

        adminGameList()

        adminStakeList()
    }
}