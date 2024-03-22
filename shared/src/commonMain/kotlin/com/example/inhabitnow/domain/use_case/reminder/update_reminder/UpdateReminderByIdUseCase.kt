package com.example.inhabitnow.domain.use_case.reminder.update_reminder

import com.example.inhabitnow.core.model.ResultModelWithException
import com.example.inhabitnow.domain.model.exceptions.SaveReminderException
import com.example.inhabitnow.domain.model.reminder.ReminderModel

interface UpdateReminderByIdUseCase {
    suspend operator fun invoke(reminderModel: ReminderModel): ResultModelWithException<Unit, SaveReminderException>
}