package com.example.inhabitnow.data.repository.record

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.data_source.record.RecordDataSource
import com.example.inhabitnow.data.model.record.RecordModel
import com.example.inhabitnow.data.util.toRecordTable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class DefaultRecordRepository(
    private val recordDataSource: RecordDataSource,
    private val json: Json,
    private val defaultDispatcher: CoroutineDispatcher
) : RecordRepository {

    override suspend fun saveRecord(recordModel: RecordModel): ResultModel<Unit> =
        withContext(defaultDispatcher) {
            recordDataSource.insertRecord(recordModel.toRecordTable(json))
        }

}