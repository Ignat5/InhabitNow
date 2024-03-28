package com.example.inhabitnow.data.data_source.record

import com.example.inhabitnow.core.model.ResultModel
import database.RecordTable
import kotlinx.coroutines.flow.Flow

interface RecordDataSource {
    fun readRecordsByDate(targetEpochDay: Long): Flow<List<RecordTable>>
    fun readRecordByTaskIdAndDate(taskId: String, targetEpochDay: Long): Flow<RecordTable?>
    suspend fun insertRecord(recordTable: RecordTable): ResultModel<Unit>
}