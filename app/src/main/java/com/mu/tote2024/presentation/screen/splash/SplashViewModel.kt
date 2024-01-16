package com.mu.tote2024.presentation.screen.splash

import androidx.lifecycle.ViewModel
import com.mu.tote2024.domain.usecase.auth_usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    authUseCase: AuthUseCase
) : ViewModel() {
    var isAuth = false

    init {
        isAuth = authUseCase.getCurrentUserId().isNotBlank()
    }
}