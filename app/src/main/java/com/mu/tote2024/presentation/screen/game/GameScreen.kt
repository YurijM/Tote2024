package com.mu.tote2024.presentation.screen.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.R
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.components.AppTextField
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants.GROUP_N
import com.mu.tote2024.presentation.utils.asTime
import com.mu.tote2024.presentation.utils.withParam
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel = hiltViewModel()
) {
    var isLoading by remember { mutableStateOf(false) }
    val stateGame by viewModel.stateGame.collectAsState()
    val state by viewModel.state.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()

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
            }

            resultGame is UiState.Success -> {
                isLoading = false
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
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = viewModel.game.start.toLong())

            DatePickerDialog(
                onDismissRequest = {
                    showDatePicker = false
                },
                confirmButton = {
                    TextButton(onClick = {
                        selectedDate = datePickerState.selectedDateMillis!!
                        viewModel.onEvent(GameEvent.OnStartChange(selectedDate.toString()))
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
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                        ) {
                            StartGame(
                                date = viewModel.game.start.asTime(),
                                onClick = { showDatePicker = true }
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                AppTextField(
                                    modifier = Modifier.fillMaxWidth(.3f),
                                    label = "№ игры",
                                    textAlign = TextAlign.Center,
                                    value = viewModel.game.gameId,
                                    onChange = { newValue ->
                                        viewModel.onEvent(GameEvent.OnGameIdChange(newValue))
                                    },
                                    errorMessage = viewModel.errorGameId,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.NumberPassword,
                                    )
                                )
                                val options = listOf("A", "B", "C", "D", "E", "F")
                                var expanded by remember { mutableStateOf(false) }
                                var selectedOptionText by remember { mutableStateOf(options[0]) }

                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = {
                                        expanded = !expanded
                                    }
                                ) {
                                    OutlinedTextField(
                                        modifier = Modifier
                                            .fillMaxWidth(1f)
                                            .menuAnchor(),
                                        readOnly = true,
                                        value = GROUP_N.withParam(selectedOptionText),
                                        onValueChange = { },
                                        label = { Text("Группа") },
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(
                                                expanded = expanded
                                            )
                                        },
                                        shape = ShapeDefaults.Medium,
                                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                                    )
                                    ExposedDropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = {
                                            expanded = false
                                        }
                                    ) {
                                        options.forEach { selectionOption ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    selectedOptionText = selectionOption
                                                    expanded = false
                                                },
                                                text = {
                                                    Text(
                                                        text = GROUP_N.withParam(selectionOption),
                                                        style = TextStyle(
                                                            platformStyle = PlatformTextStyle(
                                                                includeFontPadding = false,
                                                            ),
                                                        )
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }
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

@Composable
private fun StartGame(
    date: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "начало матча"
            )
            Text(
                text = date
            )
        }
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
    }
}
