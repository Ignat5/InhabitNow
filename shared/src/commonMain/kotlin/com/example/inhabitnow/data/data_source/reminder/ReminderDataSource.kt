package com.example.inhabitnow.data.data_source.reminder

import com.example.inhabitnow.core.model.ResultModel
import database.ReminderTable

interface ReminderDataSource {
    suspend fun insertReminder(reminderTable: ReminderTable): ResultModel<Unit>
}