package com.example.inhabitnow.data.data_source.record

import com.example.inhabitnow.core.model.ResultModel
import database.RecordTable
import kotlinx.coroutines.flow.Flow

interface RecordDataSource {
    fun readRecordsByDate(targetEpochDay: Long): Flow<List<RecordTable>>
    fun readRecordByTaskIdAndDate(taskId: String, targetEpochDay: Long): Flow<RecordTable?>
    fun readRecordsByTaskId(taskId: String): Flow<List<RecordTable>>
    suspend fun insertRecord(recordTable: RecordTable): ResultModel<Unit>
    suspend fun updateRecordEntryById(recordId: String, entry: String): ResultModel<Unit>
    suspend fun deleteRecordById(recordId: String): ResultModel<Unit>
    suspend fun deleteRecordsByTaskId(taskId: String): ResultModel<Unit>
    suspend fun deleteRecordsBeforeDateByTaskId(
        taskId: String,
        targetEpochDay: Long
    ): ResultModel<Unit>
    suspend fun deleteRecordsAfterDateByTaskId(
        taskId: String,
        targetEpochDay: Long
    ): ResultModel<Unit>
    suspend fun deleteRecordsBeforeAfterDateByTaskId(
        taskId: String,
        targetEpochDay: Long
    ): ResultModel<Unit>
}