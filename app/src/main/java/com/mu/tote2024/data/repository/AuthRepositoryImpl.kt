package com.mu.tote2024.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mu.tote2024.data.utils.Constants.CURRENT_ID
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_FUN_CREATE_GAMBLER
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_FUN_CREATE_USER_WITH_EMAIL_AND_PASSWORD
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_GAMBLER_IS_NOT_FOUND
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_NEW_GAMBLER_IS_NOT_CREATED
import com.mu.tote2024.data.utils.Constants.GAMBLER
import com.mu.tote2024.data.utils.Constants.Nodes.NODE_GAMBLERS
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.domain.repository.AuthRepository
import com.mu.tote2024.presentation.ui.common.UiState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : AuthRepository {
    override fun createGambler(
        email: String,
        password: String
    ): Flow<UiState<Boolean>> = callbackFlow {
        trySend(UiState.Loading)

        firebaseAuth.createUserWithEmailAndPassword(
            email,
            password
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = firebaseAuth.currentUser

                if (user != null) {
                    GAMBLER = GAMBLER.copy(
                        gamblerId = user.uid,
                        email = email
                    )

                    firebaseDatabase.reference
                        .child(NODE_GAMBLERS)
                        .child(user.uid)
                        .setValue(GAMBLER)

                    trySend(UiState.Success(true))
                } else {
                    trySend(UiState.Error(ERROR_NEW_GAMBLER_IS_NOT_CREATED))
                }
            } else {

                trySend(UiState.Error(ERROR_FUN_CREATE_USER_WITH_EMAIL_AND_PASSWORD))
            }
        }.addOnFailureListener {
            trySend(UiState.Error(it.message ?: ERROR_FUN_CREATE_GAMBLER))
        }

        awaitClose {
            close()
        }
    }

    override fun authGambler(
        email: String,
        password: String
    ): Flow<UiState<Boolean>> = callbackFlow {
        trySend(UiState.Loading)

        firebaseAuth.signInWithEmailAndPassword(
            email,
            password
        ).addOnSuccessListener {
            CURRENT_ID = firebaseAuth.currentUser?.uid.toString()

            trySend(UiState.Success(CURRENT_ID.isNotBlank()))
        }.addOnFailureListener {
            trySend(UiState.Error(it.message ?: ""))
        }
        awaitClose {
            close()
        }
    }

    //override fun getGambler(gamblerId: String): Flow<UiState<Boolean>> = callbackFlow {
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
