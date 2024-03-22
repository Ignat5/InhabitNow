package com.example.inhabitnow.domain.use_case.update_task_date

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.repository.task.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DefaultUpdateTaskDateUseCase(
    private val taskRepository: TaskRepository,
    private val externalScope: CoroutineScope
) : UpdateTaskDateUseCase {

    override suspend operator fun invoke(
        taskId: String,
        requestBody: UpdateTaskDateUseCase.RequestBody
    ): ResultModel<Unit> {
        val resultModel = when (requestBody) {
            is UpdateTaskDateUseCase.RequestBody.StartDate -> {
                taskRepository.updateTaskStartDateById(
                    taskId = taskId,
                    taskStartDate = requestBody.date
                )
            }

            is UpdateTaskDateUseCase.RequestBody.EndDate -> {
                taskRepository.updateTaskEndDateById(
                    taskId = taskId,
                    taskEndDate = requestBody.date
                )
            }

            is UpdateTaskDateUseCase.RequestBody.OneDayDate -> {
                taskRepository.updateTaskStartEndDateById(
                    taskId = taskId,
                    taskStartDate = requestBody.date,
                    taskEndDate = requestBody.date
                )
            }
        }

        if (resultModel is ResultModel.Success) {
            externalScope.launch {
                /* TODO(set up reminders) */
                /* TODO(delete records) */
            }
        }

        return resultModel
    }

}