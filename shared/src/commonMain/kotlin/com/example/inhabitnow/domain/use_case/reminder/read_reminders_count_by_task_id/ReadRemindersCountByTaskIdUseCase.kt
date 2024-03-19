package com.example.inhabitnow.domain.use_case.reminder.read_reminders_count_by_task_id

import kotlinx.coroutines.flow.Flow

interface ReadRemindersCountByTaskIdUseCase {
    operator fun invoke(taskId: String): Flow<Int>
}