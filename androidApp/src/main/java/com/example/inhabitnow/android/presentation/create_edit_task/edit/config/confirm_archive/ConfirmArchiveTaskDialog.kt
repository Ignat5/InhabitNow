package com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_archive

import androidx.compose.runtime.Composable
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder

@Composable
fun ConfirmArchiveTaskDialog(
    taskId: String,
    onResult: (ConfirmArchiveTaskScreenResult) -> Unit
) {
    BaseDialogBuilder.BaseMessageDialog(
        onDismissRequest = { onResult(ConfirmArchiveTaskScreenResult.Dismiss) },
        titleText = "Archive activity",
        messageText = "Are you sure you want to archive the activity?",
        actionButtons = BaseDialogBuilder.ActionButtons(
            confirmButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Confirm",
                    onClick = {
                        onResult(ConfirmArchiveTaskScreenResult.Confirm(taskId))
                    }
                )
            },
            dismissButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Cancel",
                    onClick = {
                        onResult(ConfirmArchiveTaskScreenResult.Dismiss)
                    }
                )
            }
        )
    )
}