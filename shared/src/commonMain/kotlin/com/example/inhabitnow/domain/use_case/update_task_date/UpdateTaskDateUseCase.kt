package com.example.inhabitnow.domain.use_case.update_task_date

import com.example.inhabitnow.core.model.ResultModel
import kotlinx.datetime.LocalDate

interface UpdateTaskDateUseCase {
    suspend operator fun invoke(
        taskId: String,
        requestType: UpdateTaskDateUseCase.RequestType
    ): ResultModel<Unit>

    sealed interface RequestType {
        val date: LocalDate?

        data class StartDate(override val date: LocalDate) : RequestType
        data class EndDate(override val date: LocalDate?) : RequestType
        data class OneDayDate(override val date: LocalDate) : RequestType
    }
}