package com.mu.tote2024.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mu.tote2024.data.utils.Constants
import com.mu.tote2024.data.utils.Constants.Nodes.NODE_STAKES
import com.mu.tote2024.domain.model.StakeModel
import com.mu.tote2024.domain.repository.StakeRepository
import com.mu.tote2024.presentation.ui.common.UiState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class StakeRepositoryImpl(
    private val firebaseDatabase: FirebaseDatabase
) : StakeRepository {
    override fun getStake(gameId: String, gamblerId: String): Flow<UiState<StakeModel>> = callbackFlow {
        trySend(UiState.Loading)

        val id = "$gameId-$gamblerId"

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.getValue(StakeModel::class.java) ?: StakeModel()
                val isSuccess = result.gameId.isNotBlank()

                if (isSuccess)
                    trySend(UiState.Success(result))
                else
                    trySend(UiState.Error(Constants.Errors.ERROR_GAME_IS_NOT_FOUND))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(UiState.Error(error.message))
            }
        }

        firebaseDatabase.reference.child(NODE_STAKES).child(id).addValueEventListener(valueEvent)

        awaitClose {
            firebaseDatabase.reference.child(NODE_STAKES).child(id).removeEventListener(valueEvent)
            close()
        }
    }

    override fun getStakeList(): Flow<UiState<List<StakeModel>>> = callbackFlow {
        trySend(UiState.Loading)

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.map {
                    it.getValue(StakeModel::class.java) ?: StakeModel()
                }

                trySend(UiState.Success(list))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(UiState.Error(error.message))
            }
        }

        firebaseDatabase.reference.child(NODE_STAKES).addValueEventListener(valueEvent)

        awaitClose {
            firebaseDatabase.reference.child(NODE_STAKES).removeEventListener(valueEvent)
            close()
        }
    }

    override fun saveStake(stake: StakeModel): Flow<UiState<Boolean>> = callbackFlow {
        trySend(UiState.Loading)

        firebaseDatabase.reference
            .child(NODE_STAKES)
            .child("${stake.gameId}-${stake.gamblerId}")
            .setValue(stake)

        trySend(UiState.Success(true))

        awaitClose {
            close()
        }
    }
}