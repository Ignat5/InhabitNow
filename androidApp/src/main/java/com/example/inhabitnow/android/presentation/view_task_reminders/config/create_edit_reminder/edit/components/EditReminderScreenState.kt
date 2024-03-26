package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.edit.components

import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.base.components.BaseCreateEditReminderScreenState
import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.domain.model.reminder.content.ReminderContentModel

data class EditReminderScreenState(
    override val hours: Int,
    override val minutes: Int,
    override val type: ReminderType,
    override val schedule: ReminderContentModel.ScheduleContent,
    override val canConfirm: Boolean
) : BaseCreateEditReminderScreenState
