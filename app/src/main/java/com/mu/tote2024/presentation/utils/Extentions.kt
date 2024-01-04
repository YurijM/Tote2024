package com.mu.tote2024.presentation.utils

import android.util.Log
import com.mu.tote2024.presentation.utils.Constants.DEBUG_TAG
import com.mu.tote2024.presentation.utils.Constants.Errors.EMAIL_INCORRECT
import com.mu.tote2024.presentation.utils.Constants.Errors.FIELD_CAN_NOT_EMPTY
import com.mu.tote2024.presentation.utils.Constants.Errors.FIELD_MUST_CONTAIN_LEAST_N_CHARS
import com.mu.tote2024.presentation.utils.Constants.Errors.PASSWORDS_DO_NOT_MATCH
import com.mu.tote2024.presentation.utils.Constants.MIN_PASSWORD_LENGTH

fun toLog(message: String) {
    Log.d(DEBUG_TAG, message)
}

fun checkIsFieldEmpty(value: String?): String {
    return if ((value != null) && value.isBlank())
        FIELD_CAN_NOT_EMPTY
    else ""
}

fun checkEmail(email: String?): String {
    return if (email != null) {
        when {
            email.isBlank() -> FIELD_CAN_NOT_EMPTY
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> EMAIL_INCORRECT

            else -> ""
        }
    } else ""
}

fun checkPassword(password: String?, passwordConfirm: String?): String {
    return if (password != null) {
        when {
            (password.isBlank()) -> FIELD_CAN_NOT_EMPTY

            password.length < MIN_PASSWORD_LENGTH -> {
                FIELD_MUST_CONTAIN_LEAST_N_CHARS.replace("%_%", MIN_PASSWORD_LENGTH.toString())
            }

            !passwordConfirm.isNullOrBlank() &&
                    (passwordConfirm.length >= MIN_PASSWORD_LENGTH) &&
                    (password != passwordConfirm) -> PASSWORDS_DO_NOT_MATCH

            else -> ""
        }
    } else ""
}