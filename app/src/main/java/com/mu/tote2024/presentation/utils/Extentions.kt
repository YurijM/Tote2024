package com.mu.tote2024.presentation.utils

import android.util.Log
import com.mu.tote2024.presentation.utils.Constants.DEBUG_TAG
import com.mu.tote2024.presentation.utils.Constants.Errors.EMAIL_INCORRECT
import com.mu.tote2024.presentation.utils.Constants.Errors.FIELD_CAN_NOT_EMPTY
import com.mu.tote2024.presentation.utils.Constants.Errors.FIELD_MUST_CONTAIN_LEAST_N_CHARS
import com.mu.tote2024.presentation.utils.Constants.Errors.PASSWORDS_DO_NOT_MATCH
import com.mu.tote2024.presentation.utils.Constants.MIN_PASSWORD_LENGTH
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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
                FIELD_MUST_CONTAIN_LEAST_N_CHARS.withParam(MIN_PASSWORD_LENGTH.toString())
            }

            !passwordConfirm.isNullOrBlank() &&
                    (passwordConfirm.length >= MIN_PASSWORD_LENGTH) &&
                    (password != passwordConfirm) -> PASSWORDS_DO_NOT_MATCH

            else -> ""
        }
    } else ""
}

fun convertDateTimeToTimestamp(datetime: String, toLocale: Boolean = false): String {
    val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    if (toLocale) simpleDateFormat.timeZone = TimeZone.getTimeZone("Europe/Moscow")
    val date = simpleDateFormat.parse(datetime)

    return date?.time.toString()
}
fun convertDateToTimestamp(datetime: String, toLocale: Boolean = false): String {
    val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    if (toLocale) simpleDateFormat.timeZone = TimeZone.getTimeZone("Europe/Moscow")
    val date = simpleDateFormat.parse(datetime)

    return date?.time.toString()
}

fun convertTimeToTimestamp(datetime: String, toLocale: Boolean = false): String {
    val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    if (toLocale) simpleDateFormat.timeZone = TimeZone.getTimeZone("Europe/Moscow")
    val date = simpleDateFormat.parse(datetime)

    return date?.time.toString()
}

fun String.asDateTime(withSeconds: Boolean = false, toLocale: Boolean = false): String {
    val format = if (withSeconds) {
        "dd.MM.y HH:mm:ss"
    } else {
        "dd.MM.y HH:mm"
    }
    val formatter = SimpleDateFormat(format, Locale.getDefault())

    if (toLocale) formatter.timeZone = TimeZone.getTimeZone("Europe/Moscow")

    return try {
        formatter.format(Date(this.toLong()))
    } catch (e: Exception) {
        toLog("Ошибка asDateTime ${e.message}")
        ""
    }
}

fun String.asDate(toLocale: Boolean = false): String {
    val format = "dd.MM.y"
    val formatter = SimpleDateFormat(format, Locale.getDefault())

    if (toLocale) formatter.timeZone = TimeZone.getTimeZone("Europe/Moscow")

    return try {
        formatter.format(Date(this.toLong()))
    } catch (e: Exception) {
        toLog("Ошибка asDate ${e.message}")
        ""
    }
}

fun String.asTime(): String {
    val format = "HH:mm"

    val formatter = SimpleDateFormat(format, Locale.getDefault())

    return try {
        formatter.format(Date(this.toLong()))
    } catch (e: Exception) {
        toLog("Ошибка asTime ${e.message}")
        ""
    }
}

fun String.withParam(param: String) = this.replace("%_%", param)

fun generateResult() : String {
    val goal1 = (0..3).random()
    val goal2 = (0..3).random()
    var result = "$goal1 : $goal2"
    if (goal1 == goal2) {
        val addGoal1 = (0..3).random()
        val addGoal2 = (0..3).random()
        result += ", доп.время $addGoal1 : $addGoal2"
        if (addGoal1 == addGoal2) {
            result += ", по пенальти ${(1..2).random()}"
        }
    }

    return result
}
