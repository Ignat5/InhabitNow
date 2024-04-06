package com.example.inhabitnow.domain.use_case.update_task_date

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.repository.record.RecordRepository
import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.domain.use_case.reminder.set_up_task_reminders.SetUpTaskRemindersUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DefaultUpdateTaskDateUseCase(
    private val taskRepository: TaskRepository,
    private val recordRepository: RecordRepository,
    private val setUpTaskRemindersUseCase: SetUpTaskRemindersUseCase,
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
                when (requestType) {
                    is UpdateTaskDateUseCase.RequestType.StartDate -> {
                        recordRepository.deleteRecordsBeforeDateByTaskId(
                            taskId = taskId,
                            targetDate = requestType.date
                        )
                    }
                    is UpdateTaskDateUseCase.RequestType.EndDate -> {
                        requestType.date?.let { date ->
                            recordRepository.deleteRecordsAfterDateByTaskId(
                                taskId = taskId,
                                targetDate = date
                            )
                        }
                    }
                    is UpdateTaskDateUseCase.RequestType.OneDayDate -> {
                        recordRepository.deleteRecordsBeforeAfterDateByTaskId(
                            taskId = taskId,
                            targetDate = requestType.date
                        )
                    }
                }
            }
            externalScope.launch {
                setUpTaskRemindersUseCase(taskId)
            }
        }

        return resultModel
    }

}