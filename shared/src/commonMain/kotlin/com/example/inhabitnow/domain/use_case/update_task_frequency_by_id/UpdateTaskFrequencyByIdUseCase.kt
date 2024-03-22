package com.example.inhabitnow.domain.use_case.update_task_frequency_by_id

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.domain.model.task.content.TaskContentModel

interface UpdateTaskFrequencyByIdUseCase {
    suspend operator fun invoke(
        taskId: String,
        content: TaskContentModel.FrequencyContent
    ): ResultModel<Unit>
}