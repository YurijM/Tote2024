package com.mu.tote2024.presentation.screen.admin.stake

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.R
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.domain.model.GameFlagsModel
import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.components.OkAndCancel
import com.mu.tote2024.presentation.components.Title
import com.mu.tote2024.presentation.ui.common.UiState

@Composable
fun AdminStakeListScreen(
    viewModel: AdminStakeListViewModel = hiltViewModel()
) {
    var isLoading by remember { mutableStateOf(false) }
    var stakeList by remember { mutableStateOf<List<StakeModel>>(listOf()) }
    var gamblers by remember { mutableStateOf<List<GamblerModel>>(listOf()) }
    var gameIds by remember { mutableStateOf<List<Int>>(listOf()) }
    var flagList by remember { mutableStateOf<List<GameFlagsModel>>(listOf()) }
    var gameId = ""

    val state by viewModel.state.collectAsState()
    val result = state.result

    LaunchedEffect(key1 = result) {
        when (result) {
            is UiState.Loading -> {
                isLoading = true
            }

            is UiState.Success -> {
                isLoading = false

                gamblers = viewModel.gamblers.sortedBy { item -> item.profile.nickname }
                gameIds = viewModel.gameIds.sortedBy { item -> item }

                flagList = viewModel.listGameFlags

                stakeList = result.data
                    .filter { it.gameId.toInt() in gameIds }
                    .sortedBy { it.gameId.toInt() }
            }

            is UiState.Error -> {
                isLoading = false
            }

            else -> {}
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Title(stringResource(id = R.string.admin_stake_list))
        OkAndCancel(
            titleOk = stringResource(id = R.string.generate_stakes),
            enabledOk = true,
            showCancel = false,
            onOK = {
                stakeList = listOf()
                viewModel.onEvent(AdminStakeListEvent.OnGenerate)
            },
            onCancel = {}
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 8.dp
                )
        ) {
            items(stakeList) { stake ->
                if (gameId != stake.gameId) {
                    gameId = stake.gameId
                    Text(text = stringResource(id = R.string.game_no, gameId))
                    gamblers.forEach { gambler ->
                        val stakeByGambler = stakeList.find { it.gameId == gameId && it.gamblerId == gambler.gamblerId }
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 4.dp),
                                textAlign = TextAlign.End,
                                text = "${gambler.profile.nickname} -"
                            )
                            Text(
                                modifier = Modifier.weight(1f),
                                text = if (
                                    stakeByGambler == null
                                    || stakeByGambler.goal1.isBlank()
                                    || stakeByGambler.goal2.isBlank()
                                )
                                    stringResource(R.string.stake_is_absent)
                                else
                                    "${stakeByGambler.goal1} : ${stakeByGambler.goal2}"
                            )
                        }
                    }
                }
            }
        }
    }

    if (isLoading) {
        AppProgressBar()
    }
}