package com.example.inhabitnow.domain.use_case.record.save_record

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.repository.record.RecordRepository
import com.example.inhabitnow.domain.model.record.content.RecordContentModel
import com.example.inhabitnow.domain.util.toRecordContentEntity
import kotlinx.datetime.LocalDate

class DefaultSaveRecordUseCase(
    private val recordRepository: RecordRepository
) : SaveRecordUseCase {

    override suspend operator fun invoke(
        taskId: String,
        targetDate: LocalDate,
        entry: RecordContentModel.Entry
    ): ResultModel<Unit> = recordRepository.saveRecord(
        taskId = taskId,
        targetDate = targetDate,
        entry = entry.toRecordContentEntity()
    )

}