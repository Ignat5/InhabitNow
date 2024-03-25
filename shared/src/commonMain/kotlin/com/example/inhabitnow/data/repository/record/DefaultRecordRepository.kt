package com.example.inhabitnow.data.repository.record

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.data_source.record.RecordDataSource
import com.example.inhabitnow.data.model.record.RecordEntity
import com.example.inhabitnow.data.util.toEpochDay
import com.example.inhabitnow.data.util.toRecordEntity
import com.example.inhabitnow.data.util.toRecordTable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json

class DefaultRecordRepository(
    private val recordDataSource: RecordDataSource,
    private val json: Json,
    private val defaultDispatcher: CoroutineDispatcher
) : RecordRepository {

    override fun readRecordsByDate(targetDate: LocalDate): Flow<List<RecordEntity>> =
        recordDataSource.readRecordsByDate(targetDate.toEpochDay()).map { allRecords ->
            if (allRecords.isNotEmpty()) {
                allRecords.map { it.toRecordEntity(json) }
            } else emptyList()
        }

    override suspend fun saveRecord(recordEntity: RecordEntity): ResultModel<Unit> =
        withContext(defaultDispatcher) {
            recordDataSource.insertRecord(recordEntity.toRecordTable(json))
        }

}