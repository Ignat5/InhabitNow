package com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_delete

import androidx.compose.runtime.Composable
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder

@Composable
fun ConfirmDeleteTaskDialog(
    taskId: String,
    onResult: (ConfirmDeleteTaskScreenResult) -> Unit
) {
    BaseDialogBuilder.BaseMessageDialog(
        onDismissRequest = { onResult(ConfirmDeleteTaskScreenResult.Dismiss) },
        titleText = "Delete activity",
        messageText = "Are you sure you want to delete the activity?",
        actionButtons = BaseDialogBuilder.ActionButtons(
            confirmButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Confirm",
                    onClick = {
                        onResult(ConfirmDeleteTaskScreenResult.Confirm(taskId))
                    }
                )
            },
            dismissButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Cancel",
                    onClick = {
                        onResult(ConfirmDeleteTaskScreenResult.Dismiss)
                    }
                )
            }
        )
    )
}