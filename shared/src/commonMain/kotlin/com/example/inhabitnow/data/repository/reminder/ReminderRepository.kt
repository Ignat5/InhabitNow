package com.example.inhabitnow.data.repository.reminder

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.model.reminder.ReminderWithContentEntity

interface ReminderRepository {
    suspend fun saveReminder(reminderWithContentEntity: ReminderWithContentEntity): ResultModel<Unit>
}