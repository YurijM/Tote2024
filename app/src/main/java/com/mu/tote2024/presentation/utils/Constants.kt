package com.mu.tote2024.presentation.utils

object Constants {
    const val DEBUG_TAG = "tote2024"
    const val EMPTY = "empty"
    const val YEAR_START = 2023
    const val MIN_PASSWORD_LENGTH = 6

    object Routes {
        const val SPLASH_SCREEN = "splash_screen"
        const val MAIN_SCREEN = "main_screen"
        const val AUTH_SCREEN = "auth_screen"
        const val SIGN_UP_SCREEN = "sign_up_screen"
        const val SIGN_IN_SCREEN = "sign_in_screen"
        const val PROFILE_SCREEN = "profile_screen"
        const val RATING_SCREEN = "rating_screen"
        const val STAKES_SCREEN = "stakes_screen"
        const val PROGNOSIS_SCREEN = "prognosis_screen"
        const val GAMES_SCREEN = "games_screen"
    }

    object Errors {
        const val FIELD_CAN_NOT_EMPTY = "Поле не может быть пустым"
        const val FIELD_MUST_CONTAIN_LEAST_N_CHARS = "Поле должно содержать не меньше %_% символов"
        const val PASSWORDS_DO_NOT_MATCH = "Значения паролей не совпадают"
        const val EMAIL_INCORRECT = "Некорректное значение email"
    }
}