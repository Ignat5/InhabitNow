package com.example.inhabitnow.data.data_source.reminder

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.data_source.base.BaseDataSource
import com.example.inhabitnow.database.InhabitNowDatabase
import database.ReminderTable
import kotlinx.coroutines.CoroutineDispatcher

class DefaultReminderDataSource(
    private val db: InhabitNowDatabase,
    private val ioDispatcher: CoroutineDispatcher
) : BaseDataSource(db, ioDispatcher), ReminderDataSource {

    private val reminderDao = db.reminderDaoQueries

    override suspend fun insertReminder(reminderTable: ReminderTable): ResultModel<Unit> =
        runQuery {
            reminderDao.insertReminder(reminderTable)
        }

}