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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.R
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_CANCEL_WHEN_PROFILE_IS_EMPTY
import com.mu.tote2024.presentation.components.AppProgressBar
import com.mu.tote2024.presentation.components.AppRadioGroup
import com.mu.tote2024.presentation.components.AppTextField
import com.mu.tote2024.presentation.components.LoadPhoto
import com.mu.tote2024.presentation.components.OkAndCancel
import com.mu.tote2024.presentation.components.TextError
import com.mu.tote2024.presentation.components.Title
import com.mu.tote2024.presentation.ui.common.UiState
import com.mu.tote2024.presentation.utils.Constants

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
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    val state by viewModel.state.collectAsState()
    val stateExit by viewModel.stateExit.collectAsState()

    val result = state.result
    val resultExit = stateExit.result

    LaunchedEffect(key1 = true) {
        when (result) {
            is UiState.Loading -> {
                isLoading = true
                error = ""
            }

            is UiState.Success -> {
                isLoading = false
                error = ""
            }

            is UiState.Error -> {
                isLoading = false
                error = result.message
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
                if (resultExit.data) {
                    toMain()
                }
            }

            is UiState.Error -> {
                isLoading = false
                error = resultExit.message

                if (resultExit.message == ERROR_CANCEL_WHEN_PROFILE_IS_EMPTY)
                    toAuth()
            }

            is UiState.Default -> {
                isLoading = false
            }
        }
    }

    /*LaunchedEffect(key1 = result) {
        when (result) {
            is UiState.Loading -> {
                isLoading = true
                error = ""
            }

            is UiState.Success -> {
                isLoading = false
                error = ""

                if (result.data) {
                    toMain()
                }
            }

            is UiState.Error -> {
                isLoading = false
                error.value = result.message

                if (result.message == ERROR_CANCEL_WHEN_PROFILE_IS_EMPTY)
                    toAuth()
            }

            else -> {}
        }
    }*/

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
            Title(title = stringResource(id = R.string.profile))
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
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(.4f)
                            .fillMaxHeight()
                    ) {
                        LoadPhoto(
                            viewModel.gambler.profile.photoUrl,
                            onSelect = { uri ->
                                if (uri != null) {
                                    viewModel.onEvent(ProfileEvent.OnPhotoChange(uri))
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ShowProfile(
                            email = viewModel.gambler.email,
                            rate = viewModel.gambler.rate
                        )
                        HorizontalDivider(
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        EditProfile(
                            gender = viewModel.gambler.profile.gender,
                            onGenderChange = { gender -> viewModel.onEvent(ProfileEvent.OnGenderChange(gender)) },
                            errorGender = viewModel.profileErrors.errorGender ?: "",
                            nickname = viewModel.gambler.profile.nickname,
                            onNicknameChange = { nickname -> viewModel.onEvent(ProfileEvent.OnNicknameChange(nickname)) },
                            errorNickname = viewModel.profileErrors.errorNickname ?: "",
                        )
                    }
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                OkAndCancel(
                    titleOk = stringResource(id = R.string.save),
                    enabledOk = viewModel.enabled,
                    onOK = { viewModel.onEvent(ProfileEvent.OnSave) },
                    onCancel = { viewModel.onEvent(ProfileEvent.OnCancel) }
                )
            }
        }
        if (isLoading) {
            AppProgressBar()
        }
    }
}

@Composable
private fun ShowProfile(
    email: String,
    rate: Int,
) {
    Text(
        text = email,
        color = MaterialTheme.colorScheme.onSurface,
    )
    if (rate > 0) {
        Text(
            text = LocalContext.current.getString(R.string.gambler_rate, rate),
            color = MaterialTheme.colorScheme.onSurface,
        )
    } else {
        TextError(
            errorMessage = LocalContext.current.getString(R.string.money_is_not_transferred_yet)
        )
    }
}

@Composable
private fun EditProfile(
    gender: String,
    onGenderChange: (String) -> Unit,
    errorGender: String,
    nickname: String,
    onNicknameChange: (String) -> Unit,
    errorNickname: String,
) {
    Text(
        text = "Пол",
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.Bold
    )
    AppRadioGroup(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        items = listOf(Constants.MALE, Constants.FEMALE),
        currentValue = gender,
        onClick = { newValue -> onGenderChange(newValue) },
        errorMessage = errorGender
    )
    AppTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        label = stringResource(id = R.string.enter_nick),
        value = nickname,
        onChange = { newValue -> onNicknameChange(newValue) },
        errorMessage = errorNickname
    )
}
