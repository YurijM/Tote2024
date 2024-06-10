package com.mu.tote2024.presentation.screen.admin.finish

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.R
import com.mu.tote2024.domain.model.FinishModel
import com.mu.tote2024.presentation.components.AppTextField
import com.mu.tote2024.presentation.components.OkAndCancel
import com.mu.tote2024.presentation.components.Title
import com.mu.tote2024.presentation.ui.common.UiState

@Composable
fun AdminFinishScreen(
    viewModel: AdminFinishViewModel = hiltViewModel(),
    //toAdminEmailList: () -> Unit
) {
    val isLoading = remember { mutableStateOf(false) }
    val state by viewModel.state.collectAsState()

    //val finish = viewModel.finish
    var finish by remember { mutableStateOf(FinishModel())}

    when (val result = state.result) {
        is UiState.Loading -> {
            isLoading.value = true
        }

        is UiState.Success -> {
            isLoading.value = false
            finish = viewModel.finish
            //if (viewModel.isExit) toAdminEmailList()
        }

        is UiState.Error -> {
            isLoading.value = false
        }

        else -> {}
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Title(title = stringResource(id = R.string.admin_finish))
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                border = BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outline
                ),
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .toggleable(
                            value = finish.isFinished,
                            onValueChange = { newValue ->
                                viewModel.onEvent(AdminFinishEvent.OnIsFinishedChange(newValue))
                            },
                            role = Role.Checkbox
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(end = 4.dp),
                        text = stringResource(R.string.tournament_finished),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Checkbox(
                        checked = finish.isFinished,
                        onCheckedChange = null
                    )
                }
                AppTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 8.dp,
                            end = 8.dp,
                            bottom = 8.dp,
                        ),
                    label = stringResource(id = R.string.admin_finish),
                    value = finish.text,
                    onChange = { newValue ->
                        viewModel.onEvent(AdminFinishEvent.OnTextChange(newValue))
                    },
                    errorMessage = "",
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                OkAndCancel(
                    enabledOk = true,
                    onOK = { viewModel.onEvent(AdminFinishEvent.OnSave) },
                    onCancel = { viewModel.onEvent(AdminFinishEvent.OnCancel) }
                )
            }
        }
    }
}