package com.mu.tote2024.di.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.mu.tote2024.data.repository.AuthRepositoryImpl
import com.mu.tote2024.domain.repository.AuthRepository
import com.mu.tote2024.domain.usecase.auth_usecase.AuthUseCase
import com.mu.tote2024.domain.usecase.auth_usecase.SignUp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseDatabase() = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage() = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        database: FirebaseDatabase,
    ): AuthRepository = AuthRepositoryImpl(auth, database)

    @Provides
    @Singleton
    fun provideAuthUseCase(authRepository: AuthRepository) = AuthUseCase(
        signUp = SignUp(authRepository)
    )
}