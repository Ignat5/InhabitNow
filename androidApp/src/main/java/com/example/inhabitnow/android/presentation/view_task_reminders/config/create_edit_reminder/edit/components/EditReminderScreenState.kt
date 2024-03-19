package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.edit.components

import com.example.inhabitnow.android.presentation.model.UIReminderContent
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.base.components.BaseCreateEditReminderScreenState
import com.example.inhabitnow.core.type.ReminderType

data class EditReminderScreenState(
    override val hours: Int,
    override val minutes: Int,
    override val type: ReminderType,
    override val schedule: UIReminderContent.Schedule,
    override val canConfirm: Boolean
) : BaseCreateEditReminderScreenState
