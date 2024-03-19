package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.components

import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.model.UIReminderContent
import com.example.inhabitnow.core.type.ReminderType

interface BaseCreateEditReminderScreenState : ScreenState {
    val hours: Int
    val minutes: Int
    val type: ReminderType
    val schedule: UIReminderContent.Schedule
    val canConfirm: Boolean
}