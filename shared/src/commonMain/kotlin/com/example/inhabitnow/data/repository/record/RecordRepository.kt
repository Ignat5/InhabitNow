package com.example.inhabitnow.data.repository.record

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.model.record.RecordEntity

interface RecordRepository {
    suspend fun saveRecord(recordEntity: RecordEntity): ResultModel<Unit>
}