package com.mu.tote2024.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mu.tote2024.data.utils.Constants
import com.mu.tote2024.data.utils.Constants.Nodes.NODE_PROGNOSIS
import com.mu.tote2024.domain.model.PrognosisModel
import com.mu.tote2024.domain.repository.PrognosisRepository
import com.mu.tote2024.presentation.ui.common.UiState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PrognosisRepositoryImpl(
    private val firebaseDatabase: FirebaseDatabase
) : PrognosisRepository {
    override fun getPrognosis(gameId: String): Flow<UiState<PrognosisModel>> = callbackFlow {
        trySend(UiState.Loading)

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.getValue(PrognosisModel::class.java) ?: PrognosisModel()
                val isSuccess = result.gameId.isNotBlank()

                if (isSuccess)
                    trySend(UiState.Success(result))
                else
                    trySend(UiState.Error(Constants.Errors.ERROR_PROGNOSIS_IS_NOT_FOUND))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(UiState.Error(error.message))
            }
        }

        firebaseDatabase.reference.child(NODE_PROGNOSIS).child(gameId).addValueEventListener(valueEvent)

        awaitClose {
            firebaseDatabase.reference.child(NODE_PROGNOSIS).child(gameId).removeEventListener(valueEvent)
            close()
        }
    }

    override fun getPrognosisList(): Flow<UiState<List<PrognosisModel>>> = callbackFlow {
        trySend(UiState.Loading)

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.map {
                    it.getValue(PrognosisModel::class.java) ?: PrognosisModel()
                }

                trySend(UiState.Success(list))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(UiState.Error(error.message))
            }
        }

        firebaseDatabase.reference.child(NODE_PROGNOSIS).addValueEventListener(valueEvent)

        awaitClose {
            firebaseDatabase.reference.child(NODE_PROGNOSIS).removeEventListener(valueEvent)
            close()
        }
    }

    override fun savePrognosis(prognosis: PrognosisModel): Flow<UiState<Boolean>> = callbackFlow {
        trySend(UiState.Loading)

        firebaseDatabase.reference
            .child(NODE_PROGNOSIS)
            .child(prognosis.gameId)
            .setValue(prognosis)

        trySend(UiState.Success(true))

        awaitClose {
            close()
        }
    }
}