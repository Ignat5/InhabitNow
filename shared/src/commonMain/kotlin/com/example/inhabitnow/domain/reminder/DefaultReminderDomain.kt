package com.example.inhabitnow.domain.reminder

import com.example.inhabitnow.data.repository.reminder.ReminderRepository
import kotlinx.coroutines.CoroutineDispatcher

class DefaultReminderDomain(
    private val reminderRepository: ReminderRepository,
    private val defaultDispatcher: CoroutineDispatcher
): ReminderDomain {
}