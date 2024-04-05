package com.example.inhabitnow.domain.use_case.restart_habit_by_id

import com.example.inhabitnow.core.model.ResultModel
import com.example.inhabitnow.data.repository.record.RecordRepository
import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.domain.use_case.reminder.set_up_task_reminders.SetUpTaskRemindersUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DefaultRestartHabitByIdUseCase(
    private val taskRepository: TaskRepository,
    private val recordRepository: RecordRepository,
    private val setUpTaskRemindersUseCase: SetUpTaskRemindersUseCase,
    private val externalScope: CoroutineScope
) : RestartHabitByIdUseCase {

    override suspend operator fun invoke(taskId: String): ResultModel<Unit> {
        val todayDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val resultModel = taskRepository.updateTaskStartEndDateById(
            taskId = taskId,
            taskStartDate = todayDate,
            taskEndDate = null
        )
        if (resultModel is ResultModel.Success) {
            externalScope.launch {
                recordRepository.deleteRecordsByTaskId(taskId)
            }
            externalScope.launch {
                setUpTaskRemindersUseCase(taskId)
            }
        }
        return resultModel
    }

}