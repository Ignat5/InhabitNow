package com.example.inhabitnow.domain.use_case.read_full_tasks_by_date

import com.example.inhabitnow.domain.model.task.derived.FullTaskModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface ReadFullTasksByDateUseCase {
    operator fun invoke(targetDate: LocalDate): Flow<List<FullTaskModel>>
}