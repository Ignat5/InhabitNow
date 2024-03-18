package com.example.inhabitnow.data.repository.reminder

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.model.reminder.ReminderEntity
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun readRemindersByTaskId(taskId: String): Flow<List<ReminderEntity>>
    suspend fun saveReminder(reminderEntity: ReminderEntity): ResultModel<Unit>
}