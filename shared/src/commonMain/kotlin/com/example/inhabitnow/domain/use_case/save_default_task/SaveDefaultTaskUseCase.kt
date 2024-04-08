package com.example.inhabitnow.domain.use_case.save_default_task

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.core.type.TaskProgressType
import com.example.inhabitnow.core.type.TaskType

interface SaveDefaultTaskUseCase {
    suspend operator fun invoke(
        requestType: RequestType
    ): ResultModel<String>

    sealed interface RequestType {
        data class CreateHabit(val taskProgressType: TaskProgressType) : RequestType
        data object CreateRecurringTask : RequestType
        data object CreateTask : RequestType
    }
}