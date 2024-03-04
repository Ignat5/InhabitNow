package com.example.inhabitnow.data.repository.reminder

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.data_source.reminder.ReminderDataSource
import com.example.inhabitnow.data.model.reminder.ReminderWithContentEntity
import com.example.inhabitnow.data.util.toReminderTable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class DefaultReminderRepository(
    private val reminderDataSource: ReminderDataSource,
    private val json: Json,
    private val defaultDispatcher: CoroutineDispatcher
) : ReminderRepository {

    override suspend fun saveReminder(reminderWithContentEntity: ReminderWithContentEntity): ResultModel<Unit> =
        withContext(defaultDispatcher) {
            reminderDataSource.insertReminder(reminderWithContentEntity.toReminderTable(json))
        }

}