package com.mu.tote2024.data.repository

import android.net.Uri
import com.google.firebase.storage.StorageReference
import com.mu.tote2024.domain.repository.CommonRepository
import com.mu.tote2024.presentation.ui.common.UiState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CommonRepositoryImpl @Inject constructor() : CommonRepository {
    override fun saveImageToStorage(path: StorageReference, uri: Uri): Flow<UiState<Boolean>> = callbackFlow {
        trySend(UiState.Loading)

        path.putFile(uri)

        trySend(UiState.Success(true))

        awaitClose {
            close()
        }
    }

    override fun getUrlFromStorage(path: StorageReference): Flow<UiState<Uri>> = callbackFlow {
        trySend(UiState.Loading)

        val uri = path.downloadUrl.await()

        trySend(UiState.Success(uri))

        awaitClose {
            close()
        }
    }
}