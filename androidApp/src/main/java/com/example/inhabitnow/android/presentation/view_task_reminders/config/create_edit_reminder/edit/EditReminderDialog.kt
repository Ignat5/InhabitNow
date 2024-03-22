package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.edit

import androidx.compose.runtime.Composable
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.base.BaseCreateEditReminderDialog
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.edit.components.EditReminderScreenEvent
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.edit.components.EditReminderScreenResult
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.edit.components.EditReminderScreenState

@Composable
fun EditReminderDialog(
    stateHolder: EditReminderStateHolder,
    onResult: (EditReminderScreenResult) -> Unit
) {
    BaseScreen(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        EditReminderDialogStateless(state, onEvent)
    }
}

@Composable
private fun EditReminderDialogStateless(
    state: EditReminderScreenState,
    onEvent: (EditReminderScreenEvent) -> Unit
) {
    BaseCreateEditReminderDialog(
        isCreate = false,
        state = state,
        onEvent = { baseEvent ->
            onEvent(EditReminderScreenEvent.BaseEvent(baseEvent))
        }
    )
}