package com.example.inhabitnow.domain.use_case.reminder.check_reminder_scheduled

import com.example.inhabitnow.data.repository.reminder.ReminderRepository
import com.example.inhabitnow.data.repository.task.TaskRepository
import com.example.inhabitnow.domain.util.DomainUtil.checkIfActive
import com.example.inhabitnow.domain.util.DomainUtil.checkIfScheduled
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate

class DefaultCheckReminderScheduledUseCase(
    private val reminderRepository: ReminderRepository,
    private val taskRepository: TaskRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : CheckReminderScheduledUseCase {

    override suspend operator fun invoke(reminderId: String, targetDate: LocalDate): Boolean {
        return withContext(defaultDispatcher) {
            reminderRepository.readReminderById(reminderId).firstOrNull()?.let { reminderEntity ->
                taskRepository.readTaskWithContentById(taskId = reminderEntity.taskId).firstOrNull()
                    ?.let { taskWithContentEntity ->
                        taskWithContentEntity.checkIfActive(targetDate).let { isTaskActive ->
                            if (isTaskActive) {
                                reminderEntity.schedule.checkIfScheduled(targetDate)
                            } else false
                        }
                    } ?: false
            } ?: false
        }
    }

}