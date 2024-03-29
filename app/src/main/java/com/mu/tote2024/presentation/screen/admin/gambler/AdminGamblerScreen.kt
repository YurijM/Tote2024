package com.mu.tote2024.presentation.screen.admin.gambler

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.mu.tote2024.R
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.components.AppTextField
import com.mu.tote2024.presentation.components.OkAndCancel
import com.mu.tote2024.presentation.components.Title
import com.mu.tote2024.presentation.ui.common.UiState

//@SuppressLint("UnrememberedMutableState")
@Composable
fun AdminGamblerScreen(
    viewModel: AdminGamblerViewModel = hiltViewModel(),
    toAdminGamblerList: () -> Unit,
    toAdminGamblerPhoto: (String) -> Unit
) {
    val isLoading = remember { mutableStateOf(false) }
    val error = remember { mutableStateOf("") }

    val state by viewModel.state.collectAsState()

    val gambler = viewModel.gambler

    when (val result = state.result) {
        is UiState.Loading -> {
            isLoading.value = true
            error.value = ""
        }

        is UiState.Success -> {
            isLoading.value = false
            error.value = ""

            toAdminGamblerList()
        }

        is UiState.Error -> {
            isLoading.value = false

            error.value = result.message
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
            Title(title = stringResource(id = R.string.profile))
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                border = BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outline
                ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(.4f)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            SubcomposeAsyncImage(
                                model = gambler.profile.photoUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .requiredSize(dimensionResource(id = R.dimen.profile_photo_size))
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        if (gambler.profile.photoUrl.isNotBlank()) {
                                            toAdminGamblerPhoto(gambler.profile.photoUrl)
                                        }
                                    },
                                contentScale = ContentScale.Crop,
                                loading = {
                                    Box(
                                        modifier = Modifier.size(48.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            strokeWidth = 2.dp
                                        )
                                    }
                                }
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 4.dp)
                        ) {
                            ShowProfile(gambler)
                            HorizontalDivider(
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = 4.dp),
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            EditProfile(
                                gambler = gambler,
                                viewModel = viewModel
                            )
                        }
                    }
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                OkAndCancel(
                    enabledOk = viewModel.errorRate.isBlank(),
                    onOK = { viewModel.onEvent(AdminGamblerEvent.OnSave) },
                    onCancel = { viewModel.onEvent(AdminGamblerEvent.OnCancel) }
                )
            }
        }
        if (isLoading.value) {
            AppProgressBar()
        }
    }
}

@Composable
private fun ShowProfile(gambler: GamblerModel) {
    Text(
        text = gambler.profile.nickname,
        color = MaterialTheme.colorScheme.onSurface,
    )
    Text(
        text = gambler.email,
        color = MaterialTheme.colorScheme.onSurface,
    )
    val sex = stringResource(R.string.sex)
    val sexValue = gambler.profile.gender
    Text(
        text = "$sex: $sexValue",
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun EditProfile(gambler: GamblerModel, viewModel: AdminGamblerViewModel) {
    AppTextField(
        label = stringResource(id = R.string.transferred_money),
        value = gambler.rate.toString(),
        onChange = { newValue ->
            try {
                Integer.parseInt(newValue)
                viewModel.onEvent(AdminGamblerEvent.OnRateChange(newValue))
            } catch (_: NumberFormatException) {
                if (newValue.isBlank()) {
                    viewModel.onEvent(AdminGamblerEvent.OnRateChange("0"))
                }
            }
        },
        errorMessage = viewModel.errorRate,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
        )
    )
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .toggleable(
                value = gambler.admin,
                onValueChange = { newValue ->
                    viewModel.onEvent(AdminGamblerEvent.OnIsAdminChange(newValue))
                },
                role = Role.Checkbox
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Checkbox(
            checked = gambler.admin,
            onCheckedChange = null
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = stringResource(R.string.admin),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}