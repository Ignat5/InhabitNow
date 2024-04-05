package com.example.inhabitnow.android.presentation.view_task_reminders.config.permission

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder

@Composable
fun NotificationPermissionRationaleDialog(onDismissRequest: () -> Unit) {
    BaseDialogBuilder.BaseMessageDialog(
        onDismissRequest = onDismissRequest,
        titleText = "Permission required",
        messageText = "If you want to be notified by reminders, you should allow the app to send you notifications",
        actionButtons = BaseDialogBuilder.ActionButtons(
            confirmButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Ok",
                    onClick = {
                        onDismissRequest()
                    }
                )
            },
            dismissButton = null
        )
    )
}