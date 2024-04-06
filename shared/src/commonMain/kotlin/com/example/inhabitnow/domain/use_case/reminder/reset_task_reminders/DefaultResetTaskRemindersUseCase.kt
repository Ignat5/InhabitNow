package com.example.inhabitnow.domain.use_case.reminder.reset_task_reminders

import com.example.inhabitnow.core.platform.ReminderManager
import com.example.inhabitnow.data.repository.reminder.ReminderRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class DefaultResetTaskRemindersUseCase(
    private val reminderRepository: ReminderRepository,
    private val reminderManager: ReminderManager,
    private val defaultDispatcher: CoroutineDispatcher
) : ResetTaskRemindersUseCase {

    override suspend operator fun invoke(taskId: String) {
        withContext(defaultDispatcher) {
            reminderRepository.readReminderIdsByTaskId(taskId).firstOrNull()
                ?.let { allReminderIds ->
                    allReminderIds.forEach { reminderId ->
                        reminderManager.resetReminderById(reminderId)
                    }
                }
        }
    }

}