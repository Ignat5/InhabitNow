package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.base.components

import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.domain.model.reminder.content.ReminderContentModel

interface BaseCreateEditReminderScreenState : ScreenState {
    val hours: Int
    val minutes: Int
    val type: ReminderType
    val schedule: ReminderContentModel.ScheduleContent
    val canConfirm: Boolean
}