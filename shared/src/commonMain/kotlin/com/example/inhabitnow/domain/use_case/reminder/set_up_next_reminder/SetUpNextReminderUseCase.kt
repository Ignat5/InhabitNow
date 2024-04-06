package com.example.inhabitnow.domain.use_case.reminder.set_up_next_reminder

interface SetUpNextReminderUseCase {
    suspend operator fun invoke(reminderId: String)
}