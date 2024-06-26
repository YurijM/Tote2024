package com.mu.tote2024.presentation.utils

object Constants {
    const val DEBUG_TAG = "tote2024"
    const val EMPTY = "empty"
    const val YEAR_START = 2023
    const val MIN_PASSWORD_LENGTH = 6

    const val MALE = "мужской"
    const val FEMALE = "женский"

    const val NO = "нет"

    const val KEY_ID = "id"
    const val KEY_GROUP = "group"
    const val KEY_PHOTO_URL = "photoUrl"

    const val ID_NEW_EMAIL = "-1"
    const val ID_NEW_GAME = "-1"

    val GROUPS = listOf("A", "B", "C", "D", "E", "F", "1/8 финала", "1/4 финала", "1/2 финала", "финал")
    const val GROUPS_COUNT = 6

    const val GROUP_N = "Группа %_%"

    object Routes {
        const val SPLASH_SCREEN = "splash_screen"
        const val MAIN_SCREEN = "main_screen"
        const val AUTH_SCREEN = "auth_screen"
        const val SIGN_UP_SCREEN = "sign_up_screen"
        const val SIGN_IN_SCREEN = "sign_in_screen"
        const val PROFILE_SCREEN = "profile_screen"
        const val RATING_SCREEN = "rating_screen"
        const val RATING_CHART_SCREEN = "rating_chart_screen"
        const val STAKE_LIST_SCREEN = "stake_list_screen"
        const val STAKE_SCREEN = "stake_screen"
        const val PROGNOSIS_SCREEN = "prognosis_screen"
        const val GAME_LIST_SCREEN = "game_list_screen"
        const val GROUP_GAME_LIST_SCREEN = "group_game_list_screen"
        const val GAME_SCREEN = "game_screen"

        const val ADMIN_MAIN_SCREEN = "admin_main_screen"
        const val ADMIN_EMAIL_LIST_SCREEN = "admin_email_list_screen"
        const val ADMIN_EMAIL_SCREEN = "admin_email_screen"
        const val ADMIN_GAMBLER_LIST_SCREEN = "admin_gambler_list_screen"
        const val ADMIN_GAMBLER_SCREEN = "admin_gambler_screen"
        const val ADMIN_GAMBLER_PHOTO_SCREEN = "admin_gambler_photo_screen"
        const val ADMIN_TEAM_LIST_SCREEN = "admin_team_list_screen"
        const val ADMIN_GAME_LIST_SCREEN = "admin_game_list_screen"
        const val ADMIN_STAKE_LIST_SCREEN = "admin_stake_list_screen"
        const val ADMIN_FINISH_SCREEN = "admin_finish_screen"
    }

    object Errors {
        const val FIELD_CAN_NOT_EMPTY = "Поле не может быть пустым"
        const val FIELD_CAN_NOT_NEGATIVE = "Значение не может быть отрицательным"
        const val FIELD_MUST_CONTAIN_LEAST_N_CHARS = "Поле должно содержать не меньше %_% символов"
        const val PASSWORDS_DO_NOT_MATCH = "Значения паролей не совпадают"
        const val EMAIL_INCORRECT = "Некорректное значение email"
        const val UNAUTHORIZED_EMAIL = "Неразрешённый email"
        const val ADD_GOAL_INCORRECT = "Количество мячей не может быть меньше, чем в основное время"
        const val THE_GAME_IS_STARTED_YET = "Ставку изменить нельзя: игра уже началась"
    }
}