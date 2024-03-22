package com.example.inhabitnow.domain.use_case.reminder.read_reminders_by_task_id

import com.example.inhabitnow.domain.model.reminder.ReminderModel
import kotlinx.coroutines.flow.Flow

interface ReadRemindersByTaskIdUseCase {
    operator fun invoke(taskId: String): Flow<List<ReminderModel>>
}