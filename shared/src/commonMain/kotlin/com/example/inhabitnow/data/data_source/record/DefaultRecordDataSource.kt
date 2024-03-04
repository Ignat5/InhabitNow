package com.example.inhabitnow.data.data_source.record

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.data_source.base.BaseDataSource
import com.example.inhabitnow.database.InhabitNowDatabase
import database.RecordTable
import kotlinx.coroutines.CoroutineDispatcher

class DefaultRecordDataSource(
    private val db: InhabitNowDatabase,
    private val ioDispatcher: CoroutineDispatcher
) : BaseDataSource(db, ioDispatcher), RecordDataSource {

    private val recordDao = db.recordDaoQueries

    override suspend fun insertRecord(recordTable: RecordTable): ResultModel<Unit> = runQuery {
        recordDao.insertRecord(recordTable)
    }

}