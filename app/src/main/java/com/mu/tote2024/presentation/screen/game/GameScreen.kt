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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.R
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.components.AppTextField
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.asTime
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel = hiltViewModel()
) {
    var isLoading by remember { mutableStateOf(false) }
    val stateGame by viewModel.stateGame.collectAsState()
    val state by viewModel.state.collectAsState()
    var game by remember { mutableStateOf(GameModel()) }
    var showDatePicker by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    //calendar.set(2000, 1, 1) // add year, month (Jan), date
    //game = game.copy(start = calendar.timeInMillis.toString())

    var selectedDate by remember {
        mutableLongStateOf(calendar.timeInMillis) // or use mutableStateOf(calendar.timeInMillis)
    }

    val resultGame = stateGame.result
    val result = state.result

    LaunchedEffect(key1 = resultGame, key2 = result) {
        when {
            result is UiState.Loading -> {
                isLoading = true
            }

            resultGame is UiState.Loading -> {
                isLoading = true
                game = game.copy(start = calendar.timeInMillis.toString())
            }

            resultGame is UiState.Success -> {
                isLoading = false

                game = resultGame.data
                selectedDate = game.start.toLong()
            }

            (result is UiState.Error || resultGame is UiState.Error) -> {
                isLoading = false
            }

            else -> {}
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (showDatePicker) {
            // set the initial date
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = game.start.toLong())

            DatePickerDialog(
                onDismissRequest = {
                    showDatePicker = false
                },
                confirmButton = {
                    TextButton(onClick = {
                        selectedDate = datePickerState.selectedDateMillis!!
                        showDatePicker = false
                    }) {
                        Text(text = stringResource(id = R.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDatePicker = false
                    }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState
                )
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            if (game.gameId.isNotBlank()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.outline
                        ),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            AppTextField(
                                modifier = Modifier.width(80.dp),
                                label = "№ игры",
                                textAlign = TextAlign.Center,
                                value = viewModel.gameId,
                                onChange = { newValue ->
                                    viewModel.onEvent(GameEvent.OnGameIdChange(newValue))
                                },
                                errorMessage = viewModel.errorGameId
                            )
                            Row(
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        modifier = Modifier.padding(bottom = 12.dp),
                                        text = "начало матча"
                                    )
                                    Text(
                                        text = selectedDate.toString().asTime()
                                    )
                                }
                                IconButton(
                                    onClick = { showDatePicker = true }
                                ) {
                                    Icon(
                                        modifier = Modifier.border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.outline,
                                            shape = RectangleShape
                                        ),
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = null
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


        /*Title("начало ${selectedDate.toString().asTime()}")

        Button(
            onClick = {
                showDatePicker = true
            }
        ) {
            Text(text = "Show Date Picker")
        }

        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)
        Text(
            text = "Selected date: ${formatter.format(Date(selectedDate))}"
        )*/
    }

    if (isLoading) {
        AppProgressBar()
    }
}
