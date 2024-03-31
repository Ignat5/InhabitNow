package com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_restart

import androidx.compose.runtime.Composable
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder

@Composable
fun ConfirmRestartHabitDialog(
    onResult: (ConfirmRestartHabitScreenResult) -> Unit
) {
    BaseDialogBuilder.BaseMessageDialog(
        onDismissRequest = { onResult(ConfirmRestartHabitScreenResult.Dismiss) },
        titleText = "Restart habit",
        messageText = "Are you sure you want to restart the habit? \n" +
                "All the progress will be lost.",
        actionButtons = BaseDialogBuilder.ActionButtons(
            confirmButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Confirm",
                    onClick = {
                        onResult(ConfirmRestartHabitScreenResult.Confirm)
                    }
                )
            },
            dismissButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Cancel",
                    onClick = {
                        onResult(ConfirmRestartHabitScreenResult.Dismiss)
                    }
                )
            }
        )
    )
}