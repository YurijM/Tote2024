package com.mu.tote2024.data.repository

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.mu.tote2024.data.utils.Constants.CURRENT_ID
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_ALL_FIELDS_MUST_BE_FILLED
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_EMAIL_IS_NOT_FOUND
import com.mu.tote2024.data.utils.Constants.Errors.ERROR_GAMBLER_IS_NOT_FOUND
import com.mu.tote2024.data.utils.Constants.GAMBLER
import com.mu.tote2024.data.utils.Constants.Nodes.FOLDER_PROFILE_PHOTO
import com.mu.tote2024.data.utils.Constants.Nodes.GAMBLER_PHOTO_URL
import com.mu.tote2024.data.utils.Constants.Nodes.NODE_EMAILS
import com.mu.tote2024.data.utils.Constants.Nodes.NODE_GAMBLERS
import com.mu.tote2024.data.utils.Constants.Nodes.NODE_PROFILE
import com.mu.tote2024.domain.model.EmailModel
import com.mu.tote2024.domain.model.GamblerModel
import com.mu.tote2024.domain.model.GamblerProfileModel
import com.mu.tote2024.domain.model.GamblerResultModel
import com.mu.tote2024.domain.repository.GamblerRepository
import com.mu.tote2024.presentation.ui.common.UiState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GamblerRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseStorage: FirebaseStorage
) : GamblerRepository {
    override fun saveGambler(gambler: GamblerModel): Flow<UiState<GamblerModel>> = callbackFlow {
        trySend(UiState.Loading)

        firebaseDatabase.reference
            .child(NODE_GAMBLERS)
            .child(gambler.gamblerId!!)
            .setValue(gambler)

        trySend(UiState.Success(gambler))

        awaitClose {
            close()
        }
    }

    override fun saveGamblerProfile(profile: GamblerProfileModel): Flow<UiState<Boolean>> = callbackFlow {
        trySend(UiState.Loading)

        firebaseDatabase.reference
            .child(NODE_GAMBLERS)
            .child(CURRENT_ID)
            .child(NODE_PROFILE)
            .setValue(profile)
        //.await()

        val (nickname, photoUrl, gender) = profile
        GAMBLER = GAMBLER.copy(profile = GamblerProfileModel(nickname, photoUrl, gender))

        if (profile.nickname.isBlank()
            || profile.photoUrl.isBlank()
            || profile.gender.isBlank()
        ) {
            trySend(UiState.Error(ERROR_ALL_FIELDS_MUST_BE_FILLED))
        } else {
            trySend(UiState.Success(true))
        }

        awaitClose {
            close()
        }
    }

    override fun saveGamblerPhoto(id: String, photoUri: Uri): Flow<UiState<Boolean>> = callbackFlow {
        trySend(UiState.Loading)

        val path = firebaseStorage.reference.child(FOLDER_PROFILE_PHOTO).child(id)
        path.putFile(photoUri).await()

        val uri = path.downloadUrl.await()

        firebaseDatabase.reference
            .child(NODE_GAMBLERS)
            .child(id)
            .child("profile")
            .child(GAMBLER_PHOTO_URL)
            .setValue(uri.toString())

        /*val profile = GAMBLER.profile
        profile.photoUrl = uri.toString()

        GAMBLER = GAMBLER.copy(profile = profile)*/

        GAMBLER = GAMBLER.copy(
            profile = GAMBLER.profile.copy(photoUrl = uri.toString())
        )

        trySend(UiState.Success(true))

        /*if ((commonRepository.saveImageToStorage(path, photoUri) as UiState.Success<*>).data == true) {
            val uri = (commonRepository.getUrlFromStorage(path) as UiState.Success<*>).data
            firebaseDatabase.reference.child(NODE_GAMBLERS).child(id).child(GAMBLER_PHOTO_URL).setValue(uri.toString())

            trySend(UiState.Success(true))
        } else {
            trySend(UiState.Error("GamblerRepository -> saveGamblerPhoto: error!!!"))
        }*/

        awaitClose {
            close()
        }
    }

    override fun saveGamblerResult(result: GamblerResultModel): Flow<UiState<GamblerResultModel>> {
        TODO("Not yet implemented")
    }

    override fun getGambler(gamblerId: String): Flow<UiState<GamblerModel>> = callbackFlow {
        trySend(UiState.Loading)
        //GAMBLER = firebaseDatabase.reference.child(NODE_GAMBLERS).child(gamblerId).get().await().getValue(GamblerModel::class.java) ?: GamblerModel()

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //GAMBLER = snapshot.getValue(GamblerModel::class.java) ?: GamblerModel()
                val gambler = snapshot.getValue(GamblerModel::class.java) ?: GamblerModel()
                //val isSuccess = GAMBLER.gamblerId?.isNotBlank() ?: false
                val isSuccess = gambler.gamblerId?.isNotBlank() ?: false

                if (isSuccess)
                    //trySend(UiState.Success(GAMBLER))
                    trySend(UiState.Success(gambler))
                else
                    trySend(UiState.Error(ERROR_GAMBLER_IS_NOT_FOUND))
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

    override fun getGamblerList(): Flow<UiState<List<GamblerModel>>> = callbackFlow {
        trySend(UiState.Loading)

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.map {
                    it.getValue(GamblerModel::class.java) ?: GamblerModel()
                }

                trySend(UiState.Success(list))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(UiState.Error(error.message))
            }
        }

        firebaseDatabase.reference.child(NODE_GAMBLERS).addValueEventListener(valueEvent)

        awaitClose {
            firebaseDatabase.reference.child(NODE_GAMBLERS).removeEventListener(valueEvent)
            close()
        }
    }

    override fun getEmail(emailId: String): Flow<UiState<EmailModel>> = callbackFlow {
        trySend(UiState.Loading)

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val email = snapshot.getValue(EmailModel::class.java) ?: EmailModel()
                val isSuccess = email.emailId.isNotBlank()

                if (isSuccess)
                    trySend(UiState.Success(email))
                else
                    trySend(UiState.Error(ERROR_EMAIL_IS_NOT_FOUND))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(UiState.Error(error.message))
            }
        }

        firebaseDatabase.reference.child(NODE_EMAILS).child(emailId).addValueEventListener(valueEvent)

        awaitClose {
            firebaseDatabase.reference.child(NODE_EMAILS).child(emailId).removeEventListener(valueEvent)
            close()
        }
    }

    override fun getEmailList(): Flow<UiState<List<EmailModel>>> = callbackFlow {
        trySend(UiState.Loading)

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.map {
                    it.getValue(EmailModel::class.java) ?: EmailModel()
                }

                trySend(UiState.Success(list))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(UiState.Error(error.message))
            }
        }

        firebaseDatabase.reference.child(NODE_EMAILS).addValueEventListener(valueEvent)

        awaitClose {
            firebaseDatabase.reference.child(NODE_EMAILS).removeEventListener(valueEvent)
            close()
        }
    }

    override fun saveEmail(email: EmailModel): Flow<UiState<EmailModel>> = callbackFlow {
        trySend(UiState.Loading)

        firebaseDatabase.reference
            .child(NODE_EMAILS)
            .child(email.emailId)
            .setValue(email)

        trySend(UiState.Success(email))

        awaitClose {
            close()
        }
    }

    override fun deleteEmail(id: String): Flow<UiState<Boolean>> = callbackFlow {
        trySend(UiState.Loading)

        firebaseDatabase.reference
            .child(NODE_EMAILS)
            .child(id)
            .removeValue()

        trySend(UiState.Success(true))

        awaitClose {
            close()
        }
    }
}