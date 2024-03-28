package com.example.inhabitnow.data.repository.record

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.core.util.randomUUID
import com.example.inhabitnow.data.data_source.record.RecordDataSource
import com.example.inhabitnow.data.model.record.RecordEntity
import com.example.inhabitnow.data.model.record.content.RecordContentEntity
import com.example.inhabitnow.data.util.toEpochDay
import com.example.inhabitnow.data.util.toJson
import com.example.inhabitnow.data.util.toRecordEntity
import com.example.inhabitnow.data.util.toRecordTable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
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

    override suspend fun saveRecord(
        taskId: String,
        targetDate: LocalDate,
        entry: RecordContentEntity.Entry
    ): ResultModel<Unit> = recordDataSource.readRecordByTaskIdAndDate(
        taskId = taskId,
        targetEpochDay = targetDate.toEpochDay()
    ).firstOrNull()?.toRecordEntity(json)?.let { recordEntity ->
        recordDataSource.insertRecord(
            recordEntity.copy(
                entry = entry,
                createdAt = Clock.System.now().toEpochMilliseconds()
            ).toRecordTable(json)
        )
    } ?: run {
        recordDataSource.insertRecord(
            RecordEntity(
                id = randomUUID(),
                taskId = taskId,
                date = targetDate,
                entry = entry,
                createdAt = Clock.System.now().toEpochMilliseconds()
            ).toRecordTable(json)
        )
    }

}