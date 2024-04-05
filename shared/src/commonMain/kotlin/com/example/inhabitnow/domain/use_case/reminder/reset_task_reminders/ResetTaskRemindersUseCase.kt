package com.example.inhabitnow.domain.use_case.reminder.reset_task_reminders

interface ResetTaskRemindersUseCase {
    suspend operator fun invoke(taskId: String)
}