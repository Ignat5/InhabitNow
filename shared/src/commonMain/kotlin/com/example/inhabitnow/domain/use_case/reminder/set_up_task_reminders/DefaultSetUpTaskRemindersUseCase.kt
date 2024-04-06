package com.example.inhabitnow.domain.use_case.reminder.set_up_task_reminders

import com.example.inhabitnow.data.repository.reminder.ReminderRepository
import com.example.inhabitnow.domain.use_case.reminder.set_up_next_reminder.SetUpNextReminderUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DefaultSetUpTaskRemindersUseCase(
    private val reminderRepository: ReminderRepository,
    private val setUpNextReminderUseCase: SetUpNextReminderUseCase,
    private val defaultDispatcher: CoroutineDispatcher
) : SetUpTaskRemindersUseCase {

    override suspend operator fun invoke(taskId: String) {
        withContext(defaultDispatcher) {
            reminderRepository.readReminderIdsByTaskId(taskId).firstOrNull()?.toSet()
                ?.let { reminderIds ->
                    reminderIds.forEach { reminderId ->
                        launch {
                            setUpNextReminderUseCase(reminderId)
                        }
                    }
                }
        }
    }

}