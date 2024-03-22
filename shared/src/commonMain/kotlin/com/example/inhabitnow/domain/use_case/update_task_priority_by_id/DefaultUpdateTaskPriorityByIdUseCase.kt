package com.example.inhabitnow.domain.use_case.update_task_priority_by_id

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.repository.task.TaskRepository

class DefaultUpdateTaskPriorityByIdUseCase(
    private val taskRepository: TaskRepository
) : UpdateTaskPriorityByIdUseCase {

    override suspend operator fun invoke(
        taskId: String,
        priority: String
    ): ResultModel<Unit> = taskRepository.updateTaskPriorityById(
        taskId = taskId,
        priority = priority
    )

}