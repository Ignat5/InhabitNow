package com.example.inhabitnow.data.repository.record

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.model.record.RecordModel

interface RecordRepository {
    suspend fun saveRecord(recordModel: RecordModel): ResultModel<Unit>
}