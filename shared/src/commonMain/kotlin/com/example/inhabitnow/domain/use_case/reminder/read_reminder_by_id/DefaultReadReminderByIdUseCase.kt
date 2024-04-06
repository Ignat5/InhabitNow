package com.example.inhabitnow.domain.use_case.reminder.read_reminder_by_id

import com.example.inhabitnow.data.repository.reminder.ReminderRepository
import com.example.inhabitnow.domain.model.reminder.ReminderModel
import com.example.inhabitnow.domain.util.toReminderModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultReadReminderByIdUseCase(
    private val reminderRepository: ReminderRepository
) : ReadReminderByIdUseCase {

    override operator fun invoke(reminderId: String): Flow<ReminderModel?> =
        reminderRepository.readReminderById(reminderId).map {
            it?.toReminderModel()
        }

}