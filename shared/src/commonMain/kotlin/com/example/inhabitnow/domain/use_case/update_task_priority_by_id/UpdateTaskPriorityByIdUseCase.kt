package com.example.inhabitnow.domain.use_case.update_task_priority_by_id

import com.example.inhabitnow.core.model.ResultModel

interface UpdateTaskPriorityByIdUseCase {
    suspend operator fun invoke(
        taskId: String,
        priority: String
    ): ResultModel<Unit>
}