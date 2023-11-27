package com.mu.tote2024.presentation.utils

import com.mu.tote2024.presentation.utils.Constants.Errors.FIELD_IS_NOT_EMPTY
import com.mu.tote2024.presentation.utils.Constants.Errors.FIELD_MUST_CONTAIN_LEAST_N_CHARS
import com.mu.tote2024.presentation.utils.Constants.Errors.PASSWORDS_DO_NOT_MATCH

fun checkEmail(email: String?): String {
    return when {
        email != null && email.isBlank() -> FIELD_IS_NOT_EMPTY
        else -> ""
    }
}

fun checkPassword(password: String?, passwordConfirm: String?): String {
    return if (password != null) {
        when {
            (password.isBlank()) -> FIELD_IS_NOT_EMPTY
            password.length < Constants.MIN_PASSWORD_LENGTH -> FIELD_MUST_CONTAIN_LEAST_N_CHARS
            !passwordConfirm.isNullOrBlank() &&
                    (passwordConfirm.length >= Constants.MIN_PASSWORD_LENGTH) &&
                    (password != passwordConfirm) -> PASSWORDS_DO_NOT_MATCH

            else -> ""
        }
    } else ""
}