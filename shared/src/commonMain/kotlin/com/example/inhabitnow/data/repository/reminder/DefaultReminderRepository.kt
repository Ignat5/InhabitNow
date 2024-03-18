package com.example.inhabitnow.data.repository.reminder

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.data_source.reminder.ReminderDataSource
import com.example.inhabitnow.data.model.reminder.ReminderEntity
import com.example.inhabitnow.data.util.toReminderEntity
import com.example.inhabitnow.data.util.toReminderTable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
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

    override suspend fun saveReminder(reminderEntity: ReminderEntity): ResultModel<Unit> =
        withContext(defaultDispatcher) {
            reminderDataSource.insertReminder(reminderEntity.toReminderTable(json))
        }

}