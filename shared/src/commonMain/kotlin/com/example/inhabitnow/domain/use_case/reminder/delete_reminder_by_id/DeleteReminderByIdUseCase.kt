package com.example.inhabitnow.domain.use_case.reminder.delete_reminder_by_id

import com.example.inhabitnow.core.model.ResultModel

interface DeleteReminderByIdUseCase {
    suspend operator fun invoke(reminderId: String): ResultModel<Unit>
}