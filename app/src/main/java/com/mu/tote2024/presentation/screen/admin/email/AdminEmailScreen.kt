package com.mu.tote2024.presentation.screen.admin.email

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.R
import com.mu.tote2024.presentation.components.Title
import com.mu.tote2024.presentation.components.AppTextField
import com.mu.tote2024.presentation.components.OkAndCancel
import com.mu.tote2024.presentation.ui.common.UiState

@SuppressLint("UnrememberedMutableState")
@Composable
fun AdminEmailScreen(
    viewModel: AdminEmailViewModel = hiltViewModel(),
    toAdminEmailList: () -> Unit
) {
    val isLoading = remember { mutableStateOf(false) }
    val error = remember { mutableStateOf("") }

    val state by viewModel.state.collectAsState()

    val email by mutableStateOf(viewModel.email)

    when (val result = state.result) {
        is UiState.Loading -> {
            isLoading.value = true
            error.value = ""
        }

        is UiState.Success -> {
            isLoading.value = false
            error.value = ""

            if (viewModel.isExit) toAdminEmailList()
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
            Title(title = stringResource(id = R.string.email))
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                border = BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outline
                ),
            ) {
                AppTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    label = stringResource(id = R.string.email),
                    value = email.email,
                    onChange = { newValue ->
                        viewModel.onEvent(AdminEmailEvent.OnEmailChange(newValue))
                    },
                    errorMessage = viewModel.errorEmail,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                    )
                )
                Divider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                OkAndCancel(
                    enabledOk = viewModel.errorEmail.isBlank(),
                    onSave = { viewModel.onEvent(AdminEmailEvent.OnSave) },
                    onCancel = { viewModel.onEvent(AdminEmailEvent.OnCancel) }
                )
            }
        }
    }
}