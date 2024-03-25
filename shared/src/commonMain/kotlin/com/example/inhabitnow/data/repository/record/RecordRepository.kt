package com.example.inhabitnow.data.repository.record

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.model.record.RecordEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface RecordRepository {
    fun readRecordsByDate(targetDate: LocalDate): Flow<List<RecordEntity>>
    suspend fun saveRecord(recordEntity: RecordEntity): ResultModel<Unit>
}