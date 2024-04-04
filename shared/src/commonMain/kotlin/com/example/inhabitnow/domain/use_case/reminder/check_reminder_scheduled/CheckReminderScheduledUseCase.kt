package com.example.inhabitnow.domain.use_case.reminder.check_reminder_scheduled

import kotlinx.datetime.LocalDate

interface CheckReminderScheduledUseCase {
    suspend operator fun invoke(reminderId: String, targetDate: LocalDate): Boolean
}