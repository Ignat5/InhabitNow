package com.example.inhabitnow.domain.use_case.delete_task_by_id

import com.example.inhabitnow.core.model.ResultModel

interface DeleteTaskByIdUseCase {

    suspend operator fun invoke(
        taskId: String,
    ): ResultModel<Unit>
}