package com.mu.tote2024.data.utils

import com.mu.tote2024.domain.model.GamblerModel

object Constants {
    var CURRENT_ID = ""
    var GAMBLER = GamblerModel()

    object Nodes {
        const val FOLDER_PROFILE_PHOTO = "profile_photo"

        const val NODE_GAMBLERS = "gamblers"
        const val NODE_PROFILE = "profile"
        const val NODE_EMAILS = "emails"
        const val NODE_TEAMS = "teams"
        const val NODE_GAMES = "games"
        const val NODE_STAKES = "stakes"
        const val NODE_PROGNOSIS = "prognosis"

        const val GAMBLER_PHOTO_URL = "photoUrl"
    }
    object Errors {
        const val ERROR_FUN_CREATE_USER_WITH_EMAIL_AND_PASSWORD = "Ошибка выполнения функции createUserWithEmailAndPassword"
        const val ERROR_FUN_CREATE_GAMBLER = "Ошибка выполнения функции createGambler"
        const val ERROR_NEW_GAMBLER_IS_NOT_CREATED = "Новый игрок не создан"
        const val ERROR_GAMBLER_IS_NOT_FOUND = "Участник не найден"
        const val ERROR_EMAIL_IS_NOT_FOUND = "Email адрес не найден"
        const val ERROR_TEAM_IS_NOT_FOUND = "Команда не найдена"
        const val ERROR_GAME_IS_NOT_FOUND = "Игра не найдена"
        const val ERROR_PROFILE_IS_EMPTY = "Профиль не заполнен"
        const val ERROR_CANCEL_WHEN_PROFILE_IS_EMPTY = "Отмена (профиль не заполнен)"
        const val ERROR_ALL_FIELDS_MUST_BE_FILLED = "Все поля должна быть заполнены"
    }
}