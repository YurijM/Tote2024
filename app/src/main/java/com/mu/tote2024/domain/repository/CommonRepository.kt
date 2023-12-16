package com.mu.tote2024.domain.repository

import android.net.Uri
import com.google.firebase.storage.StorageReference
import com.mu.tote2024.presentation.ui.common.UiState
import kotlinx.coroutines.flow.Flow

interface CommonRepository {
    fun saveImageToStorage(path: StorageReference, uri: Uri): Flow<UiState<Boolean>>
    fun getUrlFromStorage(path: StorageReference): Flow<UiState<Uri>>
}