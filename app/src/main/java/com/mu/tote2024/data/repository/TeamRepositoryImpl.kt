package com.mu.tote2024.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mu.tote2024.data.utils.Constants
import com.mu.tote2024.domain.model.TeamModel
import com.mu.tote2024.domain.repository.TeamRepository
import com.mu.tote2024.presentation.ui.common.UiState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class TeamRepositoryImpl(
    private val firebaseDatabase: FirebaseDatabase
) : TeamRepository {
    override fun getTeam(team: String): Flow<UiState<TeamModel>> = callbackFlow {
        trySend(UiState.Loading)

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.getValue(TeamModel::class.java) ?: TeamModel()
                val isSuccess = result.team.isNotBlank()

                if (isSuccess)
                    trySend(UiState.Success(result))
                else
                    trySend(UiState.Error(Constants.Errors.ERROR_TEAM_IS_NOT_FOUND))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(UiState.Error(error.message))
            }
        }

        firebaseDatabase.reference.child(Constants.Nodes.NODE_TEAMS).child(team).addValueEventListener(valueEvent)

        awaitClose {
            firebaseDatabase.reference.child(Constants.Nodes.NODE_TEAMS).child(team).removeEventListener(valueEvent)
            close()
        }
    }

    override fun getTeamList(): Flow<UiState<List<TeamModel>>> = callbackFlow {
        trySend(UiState.Loading)

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.map {
                    it.getValue(TeamModel::class.java) ?: TeamModel()
                }

                trySend(UiState.Success(list))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(UiState.Error(error.message))
            }
        }

        firebaseDatabase.reference.child(Constants.Nodes.NODE_TEAMS).addValueEventListener(valueEvent)

        awaitClose {
            firebaseDatabase.reference.child(Constants.Nodes.NODE_TEAMS).removeEventListener(valueEvent)
            close()
        }
    }

    override fun saveTeam(team: TeamModel): Flow<UiState<TeamModel>> = callbackFlow {
        trySend(UiState.Loading)

        firebaseDatabase.reference
            .child(Constants.Nodes.NODE_TEAMS)
            .child(team.team)
            .setValue(team)

        trySend(UiState.Success(team))

        awaitClose {
            close()
        }
    }
}