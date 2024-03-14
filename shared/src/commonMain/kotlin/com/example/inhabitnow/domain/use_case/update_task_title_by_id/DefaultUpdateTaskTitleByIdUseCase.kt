package com.example.inhabitnow.domain.use_case.update_task_title_by_id

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.repository.task.TaskRepository

class DefaultUpdateTaskTitleByIdUseCase(
    private val taskRepository: TaskRepository
) : UpdateTaskTitleByIdUseCase {

    override suspend operator fun invoke(taskId: String, title: String): ResultModel<Unit> =
        taskRepository.updateTaskTitleById(
            taskId = taskId,
            title = title
        )

}