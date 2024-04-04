package com.example.inhabitnow.domain.use_case.reminder.set_up_task_reminders

interface SetUpTaskRemindersUseCase {
    suspend operator fun invoke(taskId: String)
}