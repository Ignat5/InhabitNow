package com.example.inhabitnow.android.presentation.view_task_reminders.config.confirm_delete_reminder

import androidx.compose.runtime.Composable
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder

@Composable
fun ConfirmDeleteReminderDialog(
    reminderId: String,
    onResult: (ConfirmDeleteReminderScreenResult) -> Unit
) {
    BaseDialogBuilder.BaseMessageDialog(
        onDismissRequest = { onResult(ConfirmDeleteReminderScreenResult.Dismiss) },
        titleText = "Delete reminder",
        messageText = "Are you sure you want to delete the reminder?",
        actionButtons = BaseDialogBuilder.ActionButtons(
            confirmButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Confirm",
                    onClick = {
                        onResult(
                            ConfirmDeleteReminderScreenResult.Confirm(reminderId = reminderId)
                        )
                    }
                )
            },
            dismissButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Cancel",
                    onClick = {
                        onResult(ConfirmDeleteReminderScreenResult.Dismiss)
                    }
                )
            }
        )
    )
}