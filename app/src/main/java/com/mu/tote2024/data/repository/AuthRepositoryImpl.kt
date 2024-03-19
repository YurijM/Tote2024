package com.mu.tote2024.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.mu.tote2024.data.utils.Constants.CURRENT_ID
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_FUN_CREATE_GAMBLER
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_FUN_CREATE_USER_WITH_EMAIL_AND_PASSWORD
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
                    //GAMBLER = GAMBLER.copy(
                    GAMBLER = GamblerModel(
                        gamblerId = user.uid,
                        email = email
                    )

                    firebaseDatabase.reference
                        .child(NODE_GAMBLERS)
                        .child(user.uid)
                        .setValue(GAMBLER)

                    CURRENT_ID = user.uid

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
            trySend(UiState.Error(it.message ?: "authGambler: error is not defined"))
        }
        awaitClose {
            close()
        }
    }

    override fun getCurrentUserId() = firebaseAuth.currentUser?.uid ?: ""
}
