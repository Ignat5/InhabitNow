package com.example.inhabitnow.domain.use_case.reminder.read_reminder_by_id

import com.example.inhabitnow.domain.model.reminder.ReminderModel
import kotlinx.coroutines.flow.Flow

interface ReadReminderByIdUseCase {
    operator fun invoke(reminderId: String): Flow<ReminderModel?>
}