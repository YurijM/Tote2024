package com.mu.tote2024.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_GAMBLER_IS_NOT_FOUND
import com.mu.tote2024.data.utils.Constants.GAMBLER
import com.mu.tote2024.data.utils.Constants.Nodes.NODE_GAMBLERS
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.domain.model.GamblerProfileModel
import com.mu.tote2024.domain.model.GamblerResultModel
import com.mu.tote2024.domain.repository.GamblerRepository
import com.mu.tote2024.presentation.ui.common.UiState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class GamblerRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : GamblerRepository {
    override fun saveGambler(gambler: GamblerModel): Flow<UiState<GamblerModel>> = callbackFlow {
        trySend(UiState.Loading)

        firebaseDatabase.reference
            .child(NODE_GAMBLERS)
            .child(gambler.gamblerId!!)
            .setValue(gambler)

        trySend(UiState.Success(gambler))
    }

    override fun saveGamblerProfile(profile: GamblerProfileModel): Flow<UiState<GamblerProfileModel>> {
        TODO("Not yet implemented")
    }

    override fun saveGamblerResult(result: GamblerResultModel): Flow<UiState<GamblerResultModel>> {
        TODO("Not yet implemented")
    }

    override fun getGambler(gamblerId: String): Flow<UiState<GamblerModel>> = callbackFlow {
        trySend(UiState.Loading)

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                GAMBLER = snapshot.getValue(GamblerModel::class.java) ?: GamblerModel()

                val isSuccess = GAMBLER.gamblerId?.isNotBlank() ?: false

                if (isSuccess)
                //trySend(UiState.Success(true))
                    trySend(UiState.Success(GAMBLER))
                else
                    trySend(UiState.Error(ERROR_GAMBLER_IS_NOT_FOUND.replace("%_%", gamblerId)))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(UiState.Error(error.message))
            }
        }

        firebaseDatabase.reference.child(NODE_GAMBLERS).child(gamblerId).addValueEventListener(valueEvent)

        awaitClose {
            firebaseDatabase.reference.child(NODE_GAMBLERS).child(gamblerId).removeEventListener(valueEvent)
            close()
        }
    }
}