package com.mu.tote2024.presentation.screen.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.R
import com.mu.tote2024.presentation.components.AppDropDownList
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.components.AppTextField
import com.mu.tote2024.presentation.components.OkAndCancel
import com.mu.tote2024.presentation.components.TextError
import com.mu.tote2024.presentation.components.Title
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.GROUPS
import com.mu.tote2024.presentation.utils.asDate
import com.mu.tote2024.presentation.utils.asDateTime
import com.mu.tote2024.presentation.utils.convertDateTimeToTimestamp
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel = hiltViewModel(),
    toGameList: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    val state by viewModel.state.collectAsState()

    val result = state.result
    LaunchedEffect(key1 = result) {
        when (result) {
            is UiState.Loading -> {
                isLoading = true
            }

            is UiState.Success -> {
                isLoading = false
                if (result.data) toGameList()
            }

            is UiState.Error -> {
                isLoading = false
            }

            else -> {}
        }
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()

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
                Title(title = stringResource(id = R.string.game))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline
                    ),
                ) {
                    StartGame(
                        date = viewModel.game.start.asDateTime(),
                        errorMessage = viewModel.errorStart,
                        onClick = { showDatePicker = true }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    GameIdAndGroup(
                        gameId = viewModel.game.gameId,
                        errorMessage = viewModel.errorGameId,
                        group = viewModel.game.group,
                        onChange = { newValue ->
                            viewModel.onEvent(GameEvent.OnGameIdChange(newValue))
                        },
                        onClick = { selectedItem ->
                            viewModel.onEvent(GameEvent.OnGroupChange(selectedItem))
                        }
                    )
                    TeamData(
                        teams = viewModel.teams,
                        team = viewModel.game.team1,
                        goal = viewModel.game.goal1,
                        errorMessage = viewModel.errorGoal1,
                        onTeamSelect = { team -> viewModel.onEvent(GameEvent.OnTeamChange(1, team)) },
                        onGoalSet = { goal -> viewModel.onEvent(GameEvent.OnGoalChange(false, 1, goal)) }
                    )
                    TeamData(
                        teams = viewModel.teams,
                        team = viewModel.game.team2,
                        goal = viewModel.game.goal2,
                        errorMessage = viewModel.errorGoal2,
                        onTeamSelect = { team -> viewModel.onEvent(GameEvent.OnTeamChange(2, team)) },
                        onGoalSet = { goal -> viewModel.onEvent(GameEvent.OnGoalChange(false, 2, goal)) }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(top = 8.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    if (viewModel.isExtraTime) {
                        ExtraTime(
                            goal1 = viewModel.game.addGoal1,
                            goal2 = viewModel.game.addGoal2,
                            errorMessage = viewModel.errorExtraTime,
                            onGoal1Change = { goal ->
                                viewModel.onEvent(GameEvent.OnGoalChange(true, 1, goal))
                            },
                            onGoal2Change = { goal ->
                                viewModel.onEvent(GameEvent.OnGoalChange(true, 2, goal))
                            }
                        )
                        if (viewModel.isByPenalty) {
                            ByPenalty(
                                teams = listOf(
                                    "",
                                    viewModel.game.team1,
                                    viewModel.game.team2
                                ),
                                selectedTeam = viewModel.game.penalty,
                                onClick = { selectedItem ->
                                    viewModel.onEvent(GameEvent.OnPenaltyChange(selectedItem))
                                }
                            )
                        }
                    }
                    OkAndCancel(
                        titleOk = stringResource(id = R.string.save),
                        enabledOk = viewModel.enabled,
                        onOK = { viewModel.onEvent(GameEvent.OnSave) },
                        onCancel = { viewModel.onEvent(GameEvent.OnCancel) }
                    )
                }
            }
            //}

            if (showDatePicker) {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = if (viewModel.game.start.isNotBlank()) {
                        viewModel.game.start.toLong()
                    } else {
                        calendar.timeInMillis
                    }
                )

                SetDate(
                    datePickerState = datePickerState,
                    onDismissRequest = { showDatePicker = false },
                    onClickConfirm = {
                        val date = "${(datePickerState.selectedDateMillis!!).toString().asDate()} ${viewModel.startTime}"
                        val selectedDate = convertDateTimeToTimestamp(date).toLong()
                        viewModel.onEvent(GameEvent.OnStartChange(selectedDate.toString()))
                        showDatePicker = false
                        showTimePicker = true
                    },
                    onClickCancel = { showDatePicker = false }
                )
            }

            if (showTimePicker) {
                val time = viewModel.startTime.split(":")

                var selectedHour by remember { mutableIntStateOf(time[0].toInt()) }
                var selectedMinute by remember { mutableIntStateOf(time[1].toInt()) }
                val timePickerState = rememberTimePickerState(
                    initialHour = selectedHour,
                    initialMinute = selectedMinute
                )

                SetTime(
                    timePickerState,
                    onDismissRequest = { },
                    onClickConfirm = {
                        selectedHour = timePickerState.hour
                        selectedMinute = timePickerState.minute
                        val date = "${viewModel.game.start.asDate()} $selectedHour:$selectedMinute"
                        val selectedDate = convertDateTimeToTimestamp(date).toLong()
                        viewModel.onEvent(GameEvent.OnStartChange(selectedDate.toString()))
                        showTimePicker = false
                    },
                    onClickCancel = { showTimePicker = false }
                )
            }
        }
    }
    if (isLoading) {
        AppProgressBar()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SetDate(
    datePickerState: DatePickerState,
    onDismissRequest: () -> Unit,
    onClickConfirm: () -> Unit,
    onClickCancel: () -> Unit,
) {
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onClickConfirm
            ) {
                Text(text = stringResource(id = R.string.ok))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onClickCancel
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SetTime(
    timePickerState: TimePickerState,
    onDismissRequest: () -> Unit,
    onClickConfirm: () -> Unit,
    onClickCancel: () -> Unit,
) {
    BasicAlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 20.dp,
                        bottom = 8.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimePicker(state = timePickerState)
                Row(
                    modifier = Modifier
                        //.padding(top = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onClickCancel) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    TextButton(onClick = onClickConfirm) {
                        Text(
                            text = stringResource(id = R.string.ok),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StartGame(
    date: String,
    errorMessage: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = onClick
        ) {
            Icon(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(25)
                    )
                    .padding(4.dp),
                imageVector = Icons.Outlined.Edit,
                contentDescription = null
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.game_start)
            )
            Text(
                text = date
            )
            if (errorMessage.isNotBlank()) {
                TextError(
                    errorMessage = errorMessage,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun GameIdAndGroup(
    gameId: String,
    errorMessage: String,
    group: String,
    onChange: (String) -> Unit,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center  //.spacedBy(8.dp)
        ) {
            AppTextField(
                modifier = Modifier
                    .width(108.dp)
                    .padding(end = 8.dp),
                label = stringResource(R.string.game_number),
                textAlign = TextAlign.Center,
                value = gameId,
                onChange = { newValue -> onChange(newValue) },
                errorMessage = null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                )
            )
            AppDropDownList(
                modifier = Modifier.width(160.dp),
                list = GROUPS,
                label = stringResource(R.string.group),
                selectedItem = group,
                onClick = { selectedItem -> onClick(selectedItem) }
            )
        }
        if (errorMessage.isNotBlank()) {
            TextError(
                errorMessage = errorMessage,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TeamData(
    teams: List<String>,
    team: String,
    goal: String,
    errorMessage: String,
    onTeamSelect: (String) -> Unit,
    onGoalSet: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            AppDropDownList(
                modifier = Modifier.width(160.dp),
                list = teams,
                label = stringResource(R.string.team),
                selectedItem = team,
                onClick = { item -> onTeamSelect(item) }
            )
            AppTextField(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .width(52.dp),
                label = "",
                textAlign = TextAlign.Center,
                value = goal,
                onChange = { newValue -> onGoalSet(newValue) },
                errorMessage = null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                )
            )
        }
        if (errorMessage.isNotBlank()) {
            TextError(
                errorMessage = errorMessage,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ExtraTime(
    goal1: String,
    goal2: String,
    errorMessage: String,
    onGoal1Change: (String) -> Unit,
    onGoal2Change: (String) -> Unit
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(R.string.add_time_score),
        textAlign = TextAlign.Center
    )
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppTextField(
                modifier = Modifier
                    .width(52.dp),
                label = "",
                textAlign = TextAlign.Center,
                value = goal1,
                onChange = { newValue -> onGoal1Change(newValue) },
                errorMessage = null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                )
            )
            Text(
                text = " : ",
                style = MaterialTheme.typography.displaySmall
            )
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
        if (errorMessage.isNotBlank()) {
            TextError(
                errorMessage = errorMessage,
                textAlign = TextAlign.Center
            )
        }
    }
    HorizontalDivider(
        modifier = Modifier.padding(top = 8.dp),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
fun ByPenalty(
    teams: List<String>,
    selectedTeam: String,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppDropDownList(
            modifier = Modifier.width(180.dp),
            list = teams,
            label = stringResource(R.string.by_penalty),
            selectedItem = selectedTeam,
            onClick = { selectedItem -> onClick(selectedItem) }
        )
        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}