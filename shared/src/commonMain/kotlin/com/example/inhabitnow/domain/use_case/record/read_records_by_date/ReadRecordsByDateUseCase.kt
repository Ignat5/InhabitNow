package com.example.inhabitnow.domain.use_case.record.read_records_by_date

import com.example.inhabitnow.domain.model.record.RecordModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface ReadRecordsByDateUseCase {
    operator fun invoke(targetDate: LocalDate): Flow<List<RecordModel>>
}