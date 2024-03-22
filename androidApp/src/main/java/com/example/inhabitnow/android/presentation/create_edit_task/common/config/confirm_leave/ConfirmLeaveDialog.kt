package com.example.inhabitnow.android.presentation.create_edit_task.common.config.confirm_leave

import androidx.compose.runtime.Composable
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder

@Composable
fun ConfirmLeaveDialog(
    onResult: (ConfirmLeaveScreenResult) -> Unit
) {
    BaseDialogBuilder.BaseMessageDialog(
        onDismissRequest = { onResult(ConfirmLeaveScreenResult.Dismiss) },
        titleText = "Discard",
        messageText = "Are you sure you want to discard the activity?",
        actionButtons = BaseDialogBuilder.ActionButtons(
            confirmButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Confirm",
                    onClick = { onResult(ConfirmLeaveScreenResult.Confirm) }
                )
            },
            dismissButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Cancel",
                    onClick = { onResult(ConfirmLeaveScreenResult.Dismiss) }
                )
            }
        )
    )
}