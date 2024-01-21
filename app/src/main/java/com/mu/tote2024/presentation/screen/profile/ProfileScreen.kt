package com.mu.tote2024.presentation.screen.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.R
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_CANCEL_WHEN_PROFILE_IS_EMPTY
import com.mu.tote2024.data.utils.Constants.GAMBLER
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.components.AppRadioGroup
import com.mu.tote2024.presentation.components.AppTextField
import com.mu.tote2024.presentation.components.LoadPhoto
import com.mu.tote2024.presentation.components.SaveAndCancel
import com.mu.tote2024.presentation.components.TextError
import com.mu.tote2024.presentation.ui.common.UiState

/*@Preview(
    name = "Light",
    showBackground = true
)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PreviewProfileScreen() {
    Tote2024Theme {
        ProfileScreen(
            toMain = { }
        )
    }
}*/

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    toMain: () -> Unit,
    toAuth: () -> Unit
) {
    val isLoading = remember { mutableStateOf(false) }
    val error = remember { mutableStateOf("") }

    val state by viewModel.state.collectAsState()

    when (val result = state.result) {
        is UiState.Loading -> {
            isLoading.value = true
            error.value = ""
        }

        is UiState.Success -> {
            isLoading.value = false
            error.value = ""

            if (result.data) {
                toMain()
            }
        }

        is UiState.Error -> {
            isLoading.value = false

            if (result.message == ERROR_CANCEL_WHEN_PROFILE_IS_EMPTY) toAuth()

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
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.profile),
                style = MaterialTheme.typography.titleLarge
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outline
                ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 4.dp,
                            horizontal = 8.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(.4f)
                            .fillMaxHeight()
                    ) {
                        LoadPhoto(
                            viewModel.profile.photoUrl,
                            onSelect = { uri ->
                                if (uri != null) {
                                    viewModel.onEvent(ProfileEvent.OnPhotoChange(uri))
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                        //.padding(8.dp)
                    ) {
                        Text(
                            text = GAMBLER.email,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        if (GAMBLER.rate > 0) {
                            Text(
                                text = LocalContext.current.getString(R.string.gambler_rate, GAMBLER.rate),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        } else {
                            TextError(
                                errorMessage = LocalContext.current.getString(R.string.money_is_not_transferred_yet)
                            )
                        }
                        Divider(
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = "Пол",
                            //modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        AppRadioGroup(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            items = viewModel.sex,
                            currentValue = viewModel.profile.gender,
                            onClick = { newValue ->
                                viewModel.onEvent(ProfileEvent.OnGenderChange(newValue))
                            },
                            errorMessage = viewModel.profileErrors.errorGender
                        )
                        AppTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            label = stringResource(id = R.string.enter_nick),
                            value = viewModel.profile.nickname,
                            onChange = { newValue ->
                                viewModel.onEvent(ProfileEvent.OnNicknameChange(newValue))
                            },
                            errorMessage = viewModel.profileErrors.errorNickname
                        )
                    }
                }
                Divider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                SaveAndCancel(
                    enabled = viewModel.disabled,
                    onSave = { viewModel.onEvent(ProfileEvent.OnSave) },
                    onCancel = { viewModel.onEvent(ProfileEvent.OnCancel) }
                )
            }
        }
        if (isLoading.value) {
            AppProgressBar()
        }
    }
}