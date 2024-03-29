package com.example.inhabitnow.domain.use_case.record.save_record

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.model.record.content.RecordContentEntity
import com.example.inhabitnow.data.repository.record.RecordRepository
import com.example.inhabitnow.domain.model.record.content.RecordContentModel
import com.example.inhabitnow.domain.util.toRecordContentEntity
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.LocalDate

class DefaultSaveRecordUseCase(
    private val recordRepository: RecordRepository
) : SaveRecordUseCase {

    override suspend operator fun invoke(
        taskId: String,
        targetDate: LocalDate,
        requestType: SaveRecordUseCase.RequestType
    ): ResultModel<Unit> {
        val targetRecord = recordRepository.readRecordByTaskIdAndDate(
            taskId = taskId,
            targetDate = targetDate
        ).firstOrNull()
        return when (requestType) {
            is SaveRecordUseCase.RequestType.EntryContinuous -> {
                if (targetRecord == null) {
                    recordRepository.saveRecord(
                        taskId = taskId,
                        targetDate = targetDate,
                        entry = requestType.entry.toRecordContentEntity()
                    )
                } else {
                    recordRepository.updateRecordEntryById(
                        recordId = targetRecord.id,
                        entry = requestType.entry.toRecordContentEntity()
                    )
                }
            }

            is SaveRecordUseCase.RequestType.EntryYesNo -> {
                if (targetRecord == null) {
                    recordRepository.saveRecord(
                        taskId = taskId,
                        targetDate = targetDate,
                        entry = RecordContentEntity.Entry.Done
                    )
                } else {
                    recordRepository.deleteRecordById(targetRecord.id)
                }
            }

            is SaveRecordUseCase.RequestType.EntrySkip -> {
                if (targetRecord == null) {
                    recordRepository.saveRecord(
                        taskId = taskId,
                        targetDate = targetDate,
                        entry = RecordContentEntity.Entry.Skip
                    )
                } else {
                    recordRepository.updateRecordEntryById(
                        recordId = targetRecord.id,
                        entry = RecordContentEntity.Entry.Skip
                    )
                }
            }

            is SaveRecordUseCase.RequestType.EntryFail -> {
                if (targetRecord == null) {
                    recordRepository.saveRecord(
                        taskId = taskId,
                        targetDate = targetDate,
                        entry = RecordContentEntity.Entry.Fail
                    )
                } else {
                    recordRepository.updateRecordEntryById(
                        recordId = targetRecord.id,
                        entry = RecordContentEntity.Entry.Fail
                    )
                }
            }

            is SaveRecordUseCase.RequestType.EntryReset -> {
                if (targetRecord != null) {
                    recordRepository.deleteRecordById(targetRecord.id)
                } else ResultModel.Success(Unit)
            }
        }
    }

}