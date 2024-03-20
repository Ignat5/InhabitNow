package com.example.inhabitnow.domain.use_case.reminder.read_reminders_by_task_id

import com.example.inhabitnow.data.repository.reminder.ReminderRepository
import com.example.inhabitnow.domain.model.reminder.ReminderModel
import com.example.inhabitnow.domain.util.toReminderModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DefaultReadRemindersByTaskIdUseCase(
    private val reminderRepository: ReminderRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : ReadRemindersByTaskIdUseCase {

    override operator fun invoke(taskId: String): Flow<List<ReminderModel>> =
        reminderRepository.readRemindersByTaskId(taskId).map { allReminders ->
            if (allReminders.isNotEmpty()) {
                withContext(defaultDispatcher) {
                    allReminders
                        .map { it.toReminderModel() }
                        .sortedBy { it.time }
                }
            } else emptyList()
        }

}