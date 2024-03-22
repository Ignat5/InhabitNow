package com.example.inhabitnow.domain.use_case.update_task_title_by_id

import com.example.inhabitnow.core.model.ResultModel

interface UpdateTaskTitleByIdUseCase {
    suspend operator fun invoke(taskId: String, title: String): ResultModel<Unit>
}