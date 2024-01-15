package com.mu.tote2024.presentation.screen.admin.email.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.tote2024.R
import com.mu.tote2024.domain.model.EmailModel
import com.mu.tote2024.presentation.ui.common.UiState

@Composable
fun AdminEmailListScreen(
    viewModel: AdminEmailListViewModel = hiltViewModel()
) {
    val isLoading = remember { mutableStateOf(false) }
    var emailList by remember { mutableStateOf<List<EmailModel>>(listOf()) }

    val state by viewModel.state.collectAsState()

    when (val result = state.result) {
        is UiState.Loading -> {
            isLoading.value = true
        }

        is UiState.Success -> {
            isLoading.value = false

            emailList = result.data
                .sortedBy { it.email }
        }

        is UiState.Error -> {
            isLoading.value = false
        }

        else -> {}
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        Text(
            text = stringResource(id = R.string.admin_email_list),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()

        ) {
            items(emailList) { email ->
                Text(
                    text = email.email,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        if (isLoading.value) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}