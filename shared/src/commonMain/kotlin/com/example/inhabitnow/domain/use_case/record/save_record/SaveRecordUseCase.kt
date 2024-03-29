package com.example.inhabitnow.domain.use_case.record.save_record

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.domain.model.record.content.RecordContentModel
import kotlinx.datetime.LocalDate

interface SaveRecordUseCase {
    suspend operator fun invoke(
        taskId: String,
        targetDate: LocalDate,
        requestType: RequestType
    ): ResultModel<Unit>

    sealed interface RequestType {
        data class EntryContinuous(
            val entry: RecordContentModel.Entry.HabitEntry.HabitContinuousEntry
        ) : RequestType

        data object EntryYesNo : RequestType
        data object EntrySkip : RequestType
        data object EntryFail : RequestType
        data object EntryReset : RequestType
    }

}