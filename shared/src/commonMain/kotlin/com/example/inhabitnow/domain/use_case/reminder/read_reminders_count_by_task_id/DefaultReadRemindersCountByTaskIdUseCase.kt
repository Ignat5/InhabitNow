package com.example.inhabitnow.domain.use_case.reminder.read_reminders_count_by_task_id

import com.example.inhabitnow.data.repository.reminder.ReminderRepository
import kotlinx.coroutines.flow.Flow

class DefaultReadRemindersCountByTaskIdUseCase(
    private val reminderRepository: ReminderRepository
) : ReadRemindersCountByTaskIdUseCase {

    override operator fun invoke(taskId: String): Flow<Int> =
        reminderRepository.readRemindersCountByTaskId(taskId)

}