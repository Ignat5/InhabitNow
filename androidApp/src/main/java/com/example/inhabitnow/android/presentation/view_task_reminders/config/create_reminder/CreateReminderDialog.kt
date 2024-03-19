package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder

import androidx.compose.runtime.Composable
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.create_edit_task.create.components.CreateTaskScreenEvent
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.BaseCreateEditReminderDialog
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.components.CreateReminderScreenEvent
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.components.CreateReminderScreenResult
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.components.CreateReminderScreenState

@Composable
fun CreateReminderDialog(
    stateHolder: CreateReminderStateHolder,
    onResult: (CreateReminderScreenResult) -> Unit
) {
    BaseScreen(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        CreateReminderDialogStateless(state, onEvent)
    }
}

@Composable
private fun CreateReminderDialogStateless(
    state: CreateReminderScreenState,
    onEvent: (CreateReminderScreenEvent) -> Unit
) {
    BaseCreateEditReminderDialog(
        isCreate = true,
        state = state,
        onEvent = { baseEvent ->
            onEvent(CreateReminderScreenEvent.BaseEvent(baseEvent))
        }
    )
}

