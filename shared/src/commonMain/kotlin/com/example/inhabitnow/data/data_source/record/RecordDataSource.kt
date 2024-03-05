package com.example.inhabitnow.data.data_source.record

import com.example.inhabitnow.core.model.ResultModel
import database.RecordTable

interface RecordDataSource {
    suspend fun insertRecord(recordTable: RecordTable): ResultModel<Unit>
}