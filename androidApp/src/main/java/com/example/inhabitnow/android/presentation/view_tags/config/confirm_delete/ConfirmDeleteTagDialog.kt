package com.example.inhabitnow.android.presentation.view_tags.config.confirm_delete

import androidx.compose.runtime.Composable
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder

@Composable
fun ConfirmDeleteTagDialog(
    tagId: String,
    onResult: (ConfirmDeleteTagScreenResult) -> Unit
) {
    BaseDialogBuilder.BaseMessageDialog(
        onDismissRequest = { onResult(ConfirmDeleteTagScreenResult.Dismiss) },
        titleText = "Delete tag",
        messageText = "Are you sure you want to delete the tag?",
        actionButtons = BaseDialogBuilder.ActionButtons(
            confirmButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Confirm",
                    onClick = {
                        onResult(ConfirmDeleteTagScreenResult.Confirm(tagId))
                    }
                )
            },
            dismissButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Cancel",
                    onClick = {
                        onResult(ConfirmDeleteTagScreenResult.Dismiss)
                    }
                )
            }
        )
    )
}