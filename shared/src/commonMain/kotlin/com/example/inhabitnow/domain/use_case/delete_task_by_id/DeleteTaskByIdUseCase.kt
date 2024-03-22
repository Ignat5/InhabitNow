package com.example.inhabitnow.domain.use_case.delete_task_by_id

import com.example.inhabitnow.core.model.ResultModel

private const val DEFAULT_DELETE_DELAY_MILLIS = 5000L

interface DeleteTaskByIdUseCase {

    suspend operator fun invoke(
        taskId: String,
    ): ResultModel<Unit>
}