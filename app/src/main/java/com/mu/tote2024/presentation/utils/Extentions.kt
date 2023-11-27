package com.mu.tote2024.presentation.utils

import com.mu.tote2024.presentation.utils.Constants.Errors.FIELD_IS_NOT_EMPTY
import com.mu.tote2024.presentation.utils.Constants.Errors.FIELD_MUST_CONTAIN_LEAST_N_CHARS

fun checkEmail(email: String): String {
    return when {
        email.isBlank() -> FIELD_IS_NOT_EMPTY
        else -> ""
    }
}

fun checkPassword(password: String, passwordConfirm: String): String {
    return when {
        password.isBlank() -> FIELD_IS_NOT_EMPTY
        password.length < Constants.MIN_PASSWORD_LENGTH -> FIELD_MUST_CONTAIN_LEAST_N_CHARS
        (passwordConfirm.isNotBlank() &&
                passwordConfirm.length >= Constants.MIN_PASSWORD_LENGTH &&
                password != passwordConfirm) -> "Значения паролей не совпадают"
        else -> ""
    }
}