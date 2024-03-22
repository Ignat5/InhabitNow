package com.example.inhabitnow.domain.use_case.save_task_by_id

import com.example.inhabitnow.core.model.ResultModel

interface SaveTaskByIdUseCase {
    suspend operator fun invoke(taskId: String): ResultModel<Unit>
}