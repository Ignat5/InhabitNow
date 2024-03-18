package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.components

import androidx.compose.runtime.Immutable
import com.example.inhabitnow.android.presentation.base.components.config.BaseConfigState
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.model.UIReminderContent
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.config.CreateReminderScreenConfig
import com.example.inhabitnow.core.type.ReminderType
import kotlinx.datetime.LocalTime

@Immutable
data class CreateReminderScreenState(
    val time: LocalTime,
    val type: ReminderType,
    val schedule: UIReminderContent.Schedule,
    val baseConfig: BaseConfigState<CreateReminderScreenConfig>,
    val canConfirm: Boolean
) : ScreenState
