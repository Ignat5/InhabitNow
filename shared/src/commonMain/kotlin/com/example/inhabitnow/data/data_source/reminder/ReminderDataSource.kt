package com.example.inhabitnow.data.data_source.reminder

import com.example.inhabitnow.core.model.ResultModel
import database.ReminderTable
import kotlinx.coroutines.flow.Flow

interface ReminderDataSource {
    fun readRemindersByTaskId(taskId: String): Flow<List<ReminderTable>>
    fun readRemindersCountByTaskId(taskId: String): Flow<Long>
    fun readReminderById(reminderId: String): Flow<ReminderTable?>
    fun readReminderIdsByTaskId(taskId: String): Flow<List<String>>
    fun readReminderIds(): Flow<List<String>>
    suspend fun insertReminder(reminderTable: ReminderTable): ResultModel<Unit>
    suspend fun updateReminderById(
        reminderId: String,
        reminderTime: String,
        reminderType: String,
        reminderSchedule: String
    ): ResultModel<Unit>
    suspend fun deleteReminderById(reminderId: String): ResultModel<Unit>
}