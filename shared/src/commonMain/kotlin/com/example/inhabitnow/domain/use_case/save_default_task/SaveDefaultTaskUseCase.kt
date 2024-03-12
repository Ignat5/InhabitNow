package com.example.inhabitnow.domain.use_case.save_default_task

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.core.type.TaskProgressType
import com.example.inhabitnow.core.type.TaskType

interface SaveDefaultTaskUseCase {
    suspend operator fun invoke(
        taskType: TaskType,
        taskProgressType: TaskProgressType
    ): ResultModel<String>
}