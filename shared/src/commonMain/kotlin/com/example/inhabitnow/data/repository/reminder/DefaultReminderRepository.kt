package com.example.inhabitnow.data.repository.reminder

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.data.data_source.reminder.ReminderDataSource
import com.example.inhabitnow.data.model.reminder.ReminderEntity
import com.example.inhabitnow.data.model.reminder.content.ReminderContentEntity
import com.example.inhabitnow.data.util.toJson
import com.example.inhabitnow.data.util.toReminderEntity
import com.example.inhabitnow.data.util.toReminderTable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalTime
import kotlinx.serialization.json.Json

class DefaultReminderRepository(
    private val reminderDataSource: ReminderDataSource,
    private val json: Json,
    private val defaultDispatcher: CoroutineDispatcher
) : ReminderRepository {

    override fun readRemindersByTaskId(taskId: String): Flow<List<ReminderEntity>> =
        reminderDataSource.readRemindersByTaskId(taskId).map { allReminders ->
            if (allReminders.isNotEmpty()) {
                withContext(defaultDispatcher) {
                    allReminders.map { it.toReminderEntity(json) }
                }
            } else emptyList()
        }

    override fun readRemindersCountByTaskId(taskId: String): Flow<Int> =
        reminderDataSource.readRemindersCountByTaskId(taskId).map { it.toInt() }

    override suspend fun saveReminder(reminderEntity: ReminderEntity): ResultModel<Unit> =
        withContext(defaultDispatcher) {
            reminderDataSource.insertReminder(reminderEntity.toReminderTable(json))
        }

    override suspend fun updateReminderById(
        reminderId: String,
        reminderTime: LocalTime,
        reminderType: ReminderType,
        reminderSchedule: ReminderContentEntity.ScheduleContent
    ): ResultModel<Unit> = withContext(defaultDispatcher) {
        reminderDataSource.updateReminderById(
            reminderId = reminderId,
            reminderTime = reminderTime.toJson(json),
            reminderType = reminderType.toJson(json),
            reminderSchedule = reminderSchedule.toJson(json)
        )
    }

    override suspend fun deleteReminderById(reminderId: String): ResultModel<Unit> =
        reminderDataSource.deleteReminderById(reminderId)

}