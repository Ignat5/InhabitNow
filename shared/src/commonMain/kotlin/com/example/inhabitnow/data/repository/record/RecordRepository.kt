package com.example.inhabitnow.data.repository.record

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.model.record.RecordEntity
import com.example.inhabitnow.data.model.record.content.RecordContentEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface RecordRepository {
    fun readRecordsByDate(targetDate: LocalDate): Flow<List<RecordEntity>>
    fun readRecordByTaskIdAndDate(taskId: String, targetDate: LocalDate): Flow<RecordEntity?>
    suspend fun saveRecord(
        taskId: String,
        targetDate: LocalDate,
        entry: RecordContentEntity.Entry
    ): ResultModel<Unit>
    suspend fun updateRecordEntryById(
        recordId: String,
        entry: RecordContentEntity.Entry
    ): ResultModel<Unit>
    suspend fun deleteRecordById(recordId: String): ResultModel<Unit>
    suspend fun deleteRecordsByTaskId(taskId: String): ResultModel<Unit>
}