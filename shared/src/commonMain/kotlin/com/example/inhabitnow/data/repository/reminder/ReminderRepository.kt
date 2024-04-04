package com.example.inhabitnow.data.repository.reminder

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.data.model.reminder.ReminderEntity
import com.example.inhabitnow.data.model.reminder.content.ReminderContentEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalTime

interface ReminderRepository {
    fun readRemindersByTaskId(taskId: String): Flow<List<ReminderEntity>>
    fun readRemindersCountByTaskId(taskId: String): Flow<Int>
    fun readReminderById(reminderId: String): Flow<ReminderEntity?>
    fun readReminderIdsByTaskId(taskId: String): Flow<List<String>>
    suspend fun saveReminder(
        taskId: String,
        reminderType: ReminderType,
        reminderTime: LocalTime,
        reminderSchedule: ReminderContentEntity.ScheduleContent
    ): ResultModel<String>
    suspend fun updateReminderById(
        reminderId: String,
        reminderTime: LocalTime,
        reminderType: ReminderType,
        reminderSchedule: ReminderContentEntity.ScheduleContent
    ): ResultModel<Unit>
    suspend fun deleteReminderById(reminderId: String): ResultModel<Unit>
}