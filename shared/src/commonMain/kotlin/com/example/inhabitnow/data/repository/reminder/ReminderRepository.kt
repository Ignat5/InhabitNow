package com.example.inhabitnow.data.repository.reminder

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.model.reminder.ReminderModel

interface ReminderRepository {
    suspend fun saveReminder(reminderModel: ReminderModel): ResultModel<Unit>
}