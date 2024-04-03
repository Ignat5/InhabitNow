package com.example.inhabitnow.core.platform

interface ReminderManager {

    fun setReminder(
        reminderId: String,
        epochMillis: Long
    )

    fun resetReminderById(reminderId: String)
}