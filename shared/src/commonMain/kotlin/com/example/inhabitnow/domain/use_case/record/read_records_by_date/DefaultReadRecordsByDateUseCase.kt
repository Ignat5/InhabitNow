package com.example.inhabitnow.domain.use_case.record.read_records_by_date

import com.example.inhabitnow.data.repository.record.RecordRepository
import com.example.inhabitnow.domain.model.record.RecordModel
import com.example.inhabitnow.domain.util.toRecordModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate

class DefaultReadRecordsByDateUseCase(
    private val recordRepository: RecordRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : ReadRecordsByDateUseCase {

    override operator fun invoke(targetDate: LocalDate): Flow<List<RecordModel>> =
        recordRepository.readRecordsByDate(targetDate).map { allRecords ->
            if (allRecords.isNotEmpty()) {
                withContext(defaultDispatcher) {
                    allRecords.map { it.toRecordModel() }
                }
            } else emptyList()
        }

}