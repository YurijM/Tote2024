package com.mu.tote2024.presentation.screen.stake

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.GROUPS
import com.mu.tote2024.presentation.utils.Constants.GROUPS_COUNT
import com.mu.tote2024.presentation.utils.asDateTime
import com.mu.tote2024.presentation.utils.toLog

@Composable
fun StakeScreen(
    viewModel: StakeViewModel = hiltViewModel(),
    toStakeList: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    var stake by remember { mutableStateOf(StakeModel()) }
    val state by viewModel.state.collectAsState()

    val result = state.result

    LaunchedEffect(key1 = result) {
        when (result) {
            is UiState.Loading -> {
                toLog("Loading")
                isLoading = true
            }

            is UiState.Success -> {
                toLog("Success")
                isLoading = false
                stake = result.data
            }

            is UiState.Error -> {
                toLog("Error")
                isLoading = false
                toLog("error: ${result.message}")
            }

            else -> {
                toLog("Else")
            }
        }
    }

    toLog("isLoading: $isLoading")

    if (result is UiState.Success) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        border = BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.outline
                        ),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 8.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                )
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = stringResource(R.string.game_no, stake.gameId)
                            )
                            Text(
                                modifier = Modifier.weight(1f),
                                text = if (stake.group in GROUPS.take(GROUPS_COUNT))
                                    stringResource(R.string.group_no, stake.group)
                                else stake.group,
                                textAlign = TextAlign.End
                            )
                        }
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stake.start.asDateTime(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    if (isLoading) {
        AppProgressBar()
    }
}