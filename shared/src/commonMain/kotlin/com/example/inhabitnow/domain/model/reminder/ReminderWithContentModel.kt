package com.example.inhabitnow.domain.model.reminder

import com.example.inhabitnow.domain.model.reminder.content.ReminderContentModel

data class ReminderWithContentModel(
    val reminder: ReminderModel,
    val scheduleContent: ReminderContentModel.ScheduleContent,
    val timeContent: ReminderContentModel.TimeContent
)
