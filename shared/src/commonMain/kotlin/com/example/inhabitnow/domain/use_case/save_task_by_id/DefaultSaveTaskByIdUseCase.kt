package com.example.inhabitnow.domain.use_case.save_task_by_id

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.domain.use_case.reminder.set_up_task_reminders.SetUpTaskRemindersUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DefaultSaveTaskByIdUseCase(
    private val taskRepository: TaskRepository,
    private val setUpTaskRemindersUseCase: SetUpTaskRemindersUseCase,
    private val externalScope: CoroutineScope
) : SaveTaskByIdUseCase {

    override suspend operator fun invoke(taskId: String): ResultModel<Unit> {
        val resultModel = taskRepository.saveTaskById(taskId)
        if (resultModel is ResultModel.Success) {
            externalScope.launch {
                setUpTaskRemindersUseCase(taskId)
            }
        }
        return resultModel
    }

}