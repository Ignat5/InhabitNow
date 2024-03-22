package com.example.inhabitnow.domain.use_case.update_task_description

import com.example.inhabitnow.core.model.ResultModel

interface UpdateTaskDescriptionByIdUseCase {
    suspend operator fun invoke(
        taskId: String,
        description: String
    ): ResultModel<Unit>
}