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
        requestType: UpdateTaskDateUseCase.RequestType
    ): ResultModel<Unit> {
        val resultModel = when (requestType) {
            is UpdateTaskDateUseCase.RequestType.StartDate -> {
                taskRepository.updateTaskStartDateById(
                    taskId = taskId,
                    taskStartDate = requestType.date
                )
            }

            is UpdateTaskDateUseCase.RequestType.EndDate -> {
                taskRepository.updateTaskEndDateById(
                    taskId = taskId,
                    taskEndDate = requestType.date
                )
            }

            is UpdateTaskDateUseCase.RequestType.OneDayDate -> {
                taskRepository.updateTaskStartEndDateById(
                    taskId = taskId,
                    taskStartDate = requestType.date,
                    taskEndDate = requestType.date
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