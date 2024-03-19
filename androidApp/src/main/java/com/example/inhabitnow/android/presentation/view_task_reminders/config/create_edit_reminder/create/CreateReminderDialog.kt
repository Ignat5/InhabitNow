package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.create

import androidx.compose.runtime.Composable
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.base.BaseCreateEditReminderDialog
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.create.components.CreateReminderScreenEvent
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.create.components.CreateReminderScreenResult
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.create.components.CreateReminderScreenState

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

