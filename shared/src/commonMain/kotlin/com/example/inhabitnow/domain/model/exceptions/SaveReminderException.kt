package com.example.inhabitnow.domain.model.exceptions

sealed class SaveReminderException : Exception() {
    data object ScheduleOverlap : SaveReminderException()
    data class Other(val throwable: Throwable) : SaveReminderException()
}