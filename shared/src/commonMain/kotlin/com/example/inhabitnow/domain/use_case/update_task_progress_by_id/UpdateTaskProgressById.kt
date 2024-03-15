package com.example.inhabitnow.domain.use_case.update_task_progress_by_id

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.domain.model.task.content.TaskContentModel

interface UpdateTaskProgressById {
    suspend operator fun invoke(
        taskId: String,
        progressContent: TaskContentModel.ProgressContent
    ): ResultModel<Unit>
}