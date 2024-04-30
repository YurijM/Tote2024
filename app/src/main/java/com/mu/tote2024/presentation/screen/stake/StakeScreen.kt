package com.mu.tote2024.presentation.screen.stake

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.R
import com.mu.tote2024.domain.model.GameFlagsModel
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.presentation.components.AppDropDownList
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.components.AppTextField
import com.mu.tote2024.presentation.components.OkAndCancel
import com.mu.tote2024.presentation.components.ShowFlag
import com.mu.tote2024.presentation.components.TextError
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.GROUPS
import com.mu.tote2024.presentation.utils.Constants.GROUPS_COUNT
import com.mu.tote2024.presentation.utils.asDateTime

@Composable
fun StakeScreen(
    viewModel: StakeViewModel = hiltViewModel(),
    toStakeList: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()
    val stateExit by viewModel.stateExit.collectAsState()

    val result = state.result
    val resultExit = stateExit.result

    LaunchedEffect(key1 = result) {
        when (result) {
            is UiState.Loading -> {
                isLoading = true
            }

            is UiState.Success -> {
                isLoading = false
            }

            is UiState.Error -> {
                isLoading = false
                errorMessage = result.message
            }

            else -> {}
        }
    }
    LaunchedEffect(key1 = resultExit) {
        when (resultExit) {
            is UiState.Loading -> {
                isLoading = true
            }

            is UiState.Success -> {
                isLoading = false
                toStakeList()
            }

            is UiState.Error -> {
                isLoading = false
            }

            is UiState.Default -> {
                isLoading = false
            }
        }
    }

    if (viewModel.stake.gameId.isNotBlank()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                EditCard(
                    viewModel = viewModel,
                    errorMessage = errorMessage
                )
            }

            if (viewModel.games.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.games_played_yet),
                        fontWeight = FontWeight.Bold
                    )
                }
                items(viewModel.games) { game ->
                    GamePlayed(
                        game = game,
                        flags = viewModel.flags.first { it.gameId == game.gameId },
                        team1 = viewModel.team1,
                        team2 = viewModel.team2
                    )
                }
            }
        }
    }

    if (isLoading) {
        AppProgressBar()
    }
}

@Composable
private fun EditCard(
    viewModel: StakeViewModel,
    errorMessage: String
) {
    Card(
        modifier = Modifier
            .width(400.dp)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.outline
        ),
    ) {
        GameInfo(
            id = viewModel.stake.gameId,
            start = viewModel.start,
            group = viewModel.stake.group
        )
        MainTime(
            team1 = viewModel.stake.team1,
            team2 = viewModel.stake.team2,
            goal1 = viewModel.stake.goal1,
            goal2 = viewModel.stake.goal2,
            flags = viewModel.flags.first { it.gameId == viewModel.stake.gameId },
            errorMessage = viewModel.errorMainTime,
            onGoal1Change = { goal -> viewModel.onEvent(StakeEvent.OnGoalChange(false, 1, goal)) },
            onGoal2Change = { goal -> viewModel.onEvent(StakeEvent.OnGoalChange(false, 2, goal)) }
        )
        if (viewModel.isExtraTime) {
            ExtraTime(
                addGoal1 = viewModel.stake.addGoal1,
                addGoal2 = viewModel.stake.addGoal2,
                errorMessage = viewModel.errorExtraTime,
                onAddGoal1Change = { goal -> viewModel.onEvent(StakeEvent.OnGoalChange(true, 1, goal)) },
                onAddGoal2Change = { goal -> viewModel.onEvent(StakeEvent.OnGoalChange(true, 2, goal)) }
            )
            if (viewModel.isByPenalty) {
                ByPenalty(
                    teams = listOf(
                        "",
                        viewModel.stake.team1,
                        viewModel.stake.team2
                    ),
                    selectedTeam = viewModel.stake.penalty,
                    errorMessage = viewModel.errorByPenalty,
                    onClick = { selectedItem -> viewModel.onEvent(StakeEvent.OnPenaltyChange(selectedItem)) }
                )
            }
        }
        OkAndCancel(
            titleOk = stringResource(id = R.string.save),
            enabledOk = viewModel.enabled,
            onOK = { viewModel.onEvent(StakeEvent.OnSave) },
            onCancel = { viewModel.onEvent(StakeEvent.OnCancel) }
        )
        if (errorMessage.isNotBlank()) {
            TextError(
                errorMessage = errorMessage,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun GameInfo(
    id: String,
    start: String,
    group: String
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        text = start.asDateTime(),
        textAlign = TextAlign.Center
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp
            )
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.game_no, id)
        )
        Text(
            modifier = Modifier.weight(1f),
            text = if (group in GROUPS.take(GROUPS_COUNT))
                stringResource(R.string.group_no, group)
            else group,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun MainTime(
    team1: String,
    team2: String,
    goal1: String,
    goal2: String,
    flags: GameFlagsModel,
    errorMessage: String,
    onGoal1Change: (String) -> Unit,
    onGoal2Change: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = team1,
            textAlign = TextAlign.End
        )
        ShowFlag(flags.flag1)
        Spacer(modifier = Modifier.width(8.dp))
        ShowFlag(flags.flag2)
        Text(
            modifier = Modifier.weight(1f),
            text = team2
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterEnd
        ) {
            AppTextField(
                modifier = Modifier.width(52.dp),
                label = "",
                textAlign = TextAlign.Center,
                value = goal1,
                onChange = { newValue -> onGoal1Change(newValue) },
                errorMessage = null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                )
            )
        }
        Text(
            text = " : ",
            style = MaterialTheme.typography.displaySmall
        )
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            AppTextField(
                modifier = Modifier.width(52.dp),
                label = "",
                textAlign = TextAlign.Center,
                value = goal2,
                onChange = { newValue -> onGoal2Change(newValue) },
                errorMessage = null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                )
            )
        }
    }
    if (errorMessage.isNotBlank()) {
        TextError(
            errorMessage = errorMessage,
            textAlign = TextAlign.Center
        )
    }
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
private fun ExtraTime(
    addGoal1: String,
    addGoal2: String,
    errorMessage: String,
    onAddGoal1Change: (String) -> Unit,
    onAddGoal2Change: (String) -> Unit,
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(id = R.string.add_time_score),
        textAlign = TextAlign.Center
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterEnd
        ) {
            AppTextField(
                modifier = Modifier.width(52.dp),
                label = "",
                textAlign = TextAlign.Center,
                value = addGoal1,
                onChange = { newValue -> onAddGoal1Change(newValue) },
                errorMessage = null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                )
            )
        }
        Text(
            text = " : ",
            style = MaterialTheme.typography.displaySmall
        )
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            AppTextField(
                modifier = Modifier.width(52.dp),
                label = "",
                textAlign = TextAlign.Center,
                value = addGoal2,
                onChange = { newValue -> onAddGoal2Change(newValue) },
                errorMessage = null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                )
            )
        }
    }
    if (errorMessage.isNotBlank()) {
        TextError(
            errorMessage = errorMessage,
            textAlign = TextAlign.Center
        )
    }
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
fun ByPenalty(
    teams: List<String>,
    selectedTeam: String,
    errorMessage: String,
    onClick: (String) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AppDropDownList(
            modifier = Modifier.width(180.dp),
            list = teams,
            label = stringResource(R.string.by_penalty),
            selectedItem = selectedTeam,
            onClick = { selectedItem -> onClick(selectedItem) }
        )
    }
    if (errorMessage.isNotBlank()) {
        TextError(
            errorMessage = errorMessage,
            textAlign = TextAlign.Center
        )
    }
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
private fun GamePlayed(
    game: GameModel,
    flags: GameFlagsModel,
    team1: String,
    team2: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    text = game.team1,
                    fontWeight = if (game.team1 in listOf(team1, team2))
                        FontWeight.Bold
                    else
                        FontWeight.Normal
                )
                ShowFlag(flags.flag1)
                Text(
                    text = " - "
                )
                ShowFlag(flags.flag2)
                Text(
                    modifier = Modifier.weight(1f),
                    text = game.team2,
                    fontWeight = if (game.team2 in listOf(team1, team2))
                        FontWeight.Bold
                    else
                        FontWeight.Normal
                )
            }
            Text(
                text = "${game.goal1}:${game.goal2}" +
                        if (game.addGoal1.isNotBlank())
                            ", ${stringResource(id = R.string.add_time)} ${game.addGoal1}:${game.addGoal2}" +
                                    if (game.penalty.isNotBlank())
                                        ", ${stringResource(id = R.string.by_penalty)} ${game.penalty}"
                                    else ""
                        else ""
            )
        }
    }
}