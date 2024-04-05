package com.example.inhabitnow.data.data_source.record

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.data_source.base.BaseDataSource
import com.example.inhabitnow.database.InhabitNowDatabase
import database.RecordTable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class DefaultRecordDataSource(
    private val db: InhabitNowDatabase,
    private val ioDispatcher: CoroutineDispatcher
) : BaseDataSource(db, ioDispatcher), RecordDataSource {

    private val recordDao = db.recordDaoQueries

    override fun readRecordsByDate(targetEpochDay: Long): Flow<List<RecordTable>> = readQueryList {
        recordDao.selectRecordsByDate(targetEpochDay)
    }

    override fun readRecordByTaskIdAndDate(
        taskId: String,
        targetEpochDay: Long
    ): Flow<RecordTable?> = readOneOrNullQuery {
        recordDao.selectRecordByTaskIdAndDate(
            taskId = taskId,
            targetEpochDay = targetEpochDay
        )
    }

    override fun readRecordsByTaskId(taskId: String): Flow<List<RecordTable>> = readQueryList {
        recordDao.selectRecordsByTaskId(taskId = taskId)
    }

    override suspend fun insertRecord(recordTable: RecordTable): ResultModel<Unit> = runQuery {
        recordDao.insertRecord(recordTable)
    }

    override suspend fun updateRecordEntryById(recordId: String, entry: String): ResultModel<Unit> =
        runQuery {
            recordDao.updateRecordEntryById(
                recordId = recordId,
                entry = entry
            )
        }

    override suspend fun deleteRecordById(recordId: String): ResultModel<Unit> = runQuery {
        recordDao.deleteRecordById(recordId)
    }

    override suspend fun deleteRecordsByTaskId(taskId: String): ResultModel<Unit> = runQuery {
        recordDao.deleteRecordsByTaskId(taskId)
    }

    override suspend fun deleteRecordsBeforeDateByTaskId(
        taskId: String,
        targetEpochDay: Long
    ): ResultModel<Unit> = runQuery {
        recordDao.deleteRecordsBeforeDateByTaskId(
            taskId = taskId,
            targetEpochDay = targetEpochDay
        )
    }

    override suspend fun deleteRecordsAfterDateByTaskId(
        taskId: String,
        targetEpochDay: Long
    ): ResultModel<Unit> = runQuery {
        recordDao.deleteRecordsAfterDateByTaskId(
            taskId = taskId,
            targetEpochDay = targetEpochDay
        )
    }

    override suspend fun deleteRecordsBeforeAfterDateByTaskId(
        taskId: String,
        targetEpochDay: Long
    ): ResultModel<Unit> = runTransaction {
        recordDao.apply {
            deleteRecordsBeforeDateByTaskId(taskId, targetEpochDay)
            deleteRecordsAfterDateByTaskId(taskId, targetEpochDay)
        }
    }

}