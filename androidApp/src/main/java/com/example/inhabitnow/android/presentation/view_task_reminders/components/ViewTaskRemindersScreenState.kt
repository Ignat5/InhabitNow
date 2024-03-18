package com.example.inhabitnow.android.presentation.view_task_reminders.components

import androidx.compose.runtime.Immutable
import com.example.inhabitnow.android.presentation.base.components.state.ScreenState
import com.example.inhabitnow.android.presentation.model.UIResultModel
import com.example.inhabitnow.domain.model.reminder.ReminderModel

@Immutable
data class ViewTaskRemindersScreenState(
    val allRemindersResultModel: UIResultModel<List<ReminderModel>>
) : ScreenState
