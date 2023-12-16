package com.mu.tote2024.data.utils

import com.mu.tote2024.domain.model.GamblerModel

object Constants {
    var CURRENT_ID = ""
    var GAMBLER = GamblerModel()

    object Nodes {
        const val FOLDER_PROFILE_PHOTO = "profile_photo"

        const val NODE_GAMBLERS = "gamblers"
        const val NODE_PROFILE = "profile"

        const val GAMBLER_PHOTO_URL = "photoUrl"
    }

    object Errors {
        const val ERROR_FUN_CREATE_USER_WITH_EMAIL_AND_PASSWORD = "Ошибка выполнения функции createUserWithEmailAndPassword"
        const val ERROR_FUN_CREATE_GAMBLER = "Ошибка выполнения функции createGambler"
        const val ERROR_NEW_GAMBLER_IS_NOT_CREATED = "Новый игрок не создан"
        const val ERROR_GAMBLER_IS_NOT_FOUND = "Участник (gamblerId = \"%_%\") не найден"
    }
}