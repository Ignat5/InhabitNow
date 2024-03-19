package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.components

import androidx.compose.runtime.Immutable
import com.example.inhabitnow.android.presentation.model.UIReminderContent
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.components.BaseCreateEditReminderScreenState
import com.example.inhabitnow.core.type.ReminderType

@Immutable
data class CreateReminderScreenState(
    override val hours: Int,
    override val minutes: Int,
    override val type: ReminderType,
    override val schedule: UIReminderContent.Schedule,
    override val canConfirm: Boolean
) : BaseCreateEditReminderScreenState
