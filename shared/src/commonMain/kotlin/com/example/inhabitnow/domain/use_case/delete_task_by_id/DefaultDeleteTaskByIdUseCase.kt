package com.example.inhabitnow.domain.use_case.delete_task_by_id

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.repository.task.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DefaultDeleteTaskByIdUseCase(
    private val taskRepository: TaskRepository,
    private val externalScope: CoroutineScope
) : DeleteTaskByIdUseCase {

    override suspend operator fun invoke(
        taskId: String
    ): ResultModel<Unit> {
        val resultModel = taskRepository.deleteTaskById(taskId)
        if (resultModel is ResultModel.Success) {
            externalScope.launch {
                // reset reminders
            }
        }
        return resultModel
    }

}