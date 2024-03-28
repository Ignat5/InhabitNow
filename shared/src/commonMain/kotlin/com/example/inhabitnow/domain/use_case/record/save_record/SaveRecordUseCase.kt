package com.example.inhabitnow.domain.use_case.record.save_record

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.domain.model.record.content.RecordContentModel
import kotlinx.datetime.LocalDate

interface SaveRecordUseCase {
    suspend operator fun invoke(
        taskId: String,
        targetDate: LocalDate,
        entry: RecordContentModel.Entry
    ): ResultModel<Unit>
}