package com.mu.tote2024.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mu.tote2024.data.utils.Constants
import com.mu.tote2024.domain.model.GameModel
import com.mu.tote2024.domain.repository.GameRepository
import com.mu.tote2024.presentation.ui.common.UiState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class GameRepositoryImpl(
    private val firebaseDatabase: FirebaseDatabase
) : GameRepository {
    override fun getGame(id: String): Flow<UiState<GameModel>> = callbackFlow {
        trySend(UiState.Loading)

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.getValue(GameModel::class.java) ?: GameModel()
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

        firebaseDatabase.reference.child(Constants.Nodes.NODE_GAMES).child(id).addValueEventListener(valueEvent)

        awaitClose {
            firebaseDatabase.reference.child(Constants.Nodes.NODE_TEAMS).child(id).removeEventListener(valueEvent)
            close()
        }
    }

    override fun getGameList(): Flow<UiState<List<GameModel>>> = callbackFlow {
        trySend(UiState.Loading)

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.map {
                    it.getValue(GameModel::class.java) ?: GameModel()
                }

                trySend(UiState.Success(list))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(UiState.Error(error.message))
            }
        }

        firebaseDatabase.reference.child(Constants.Nodes.NODE_GAMES).addValueEventListener(valueEvent)

        awaitClose {
            firebaseDatabase.reference.child(Constants.Nodes.NODE_GAMES).removeEventListener(valueEvent)
            close()
        }
    }

    override fun saveGame(game: GameModel): Flow<UiState<Boolean>> = callbackFlow {
        trySend(UiState.Loading)

        firebaseDatabase.reference
            .child(Constants.Nodes.NODE_GAMES)
            .child(game.gameId)
            .setValue(game)

        trySend(UiState.Success(true))

        awaitClose {
            close()
        }
    }
}