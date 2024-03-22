package com.example.inhabitnow.domain.use_case.reminder.save_reminder

import com.example.inhabitnow.core.model.ResultModelWithException
import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.domain.model.exceptions.SaveReminderException
import com.example.inhabitnow.domain.model.reminder.content.ReminderContentModel
import kotlinx.datetime.LocalTime

interface SaveReminderUseCase {
    suspend operator fun invoke(
        taskId: String,
        reminderType: ReminderType,
        reminderTime: LocalTime,
        reminderSchedule: ReminderContentModel.ScheduleContent
    ): ResultModelWithException<Unit, SaveReminderException>
}