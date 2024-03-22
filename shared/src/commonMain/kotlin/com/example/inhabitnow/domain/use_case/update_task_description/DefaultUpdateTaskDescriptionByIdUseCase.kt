package com.example.inhabitnow.domain.use_case.update_task_description

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.repository.task.TaskRepository

class DefaultUpdateTaskDescriptionByIdUseCase(
    private val taskRepository: TaskRepository
) : UpdateTaskDescriptionByIdUseCase {

    override suspend operator fun invoke(
        taskId: String,
        description: String
    ): ResultModel<Unit> = taskRepository.updateTaskDescriptionById(
        taskId = taskId,
        description = description
    )

}