package com.example.inhabitnow.domain.use_case.reminder.set_up_all_tasks_reminders

import com.example.inhabitnow.data.repository.reminder.ReminderRepository
import com.example.inhabitnow.domain.use_case.reminder.set_up_next_reminder.SetUpNextReminderUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DefaultSetUpAllRemindersUseCase(
    private val reminderRepository: ReminderRepository,
    private val setUpNextReminderUseCase: SetUpNextReminderUseCase,
    private val defaultDispatcher: CoroutineDispatcher
) : SetUpAllRemindersUseCase {

    override suspend operator fun invoke() {
        withContext(defaultDispatcher) {
            reminderRepository.readReminderIds().firstOrNull()?.let { allReminderIds ->
                allReminderIds.forEach { reminderId ->
                    launch {
                        setUpNextReminderUseCase(reminderId)
                    }
                }
            }
        }
    }

}