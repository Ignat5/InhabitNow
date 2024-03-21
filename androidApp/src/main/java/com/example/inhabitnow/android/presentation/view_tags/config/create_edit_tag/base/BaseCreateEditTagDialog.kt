package com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.base

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.base.components.BaseCreateEditTagScreenEvent
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.base.components.BaseCreateEditTagScreenState
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder
import com.example.inhabitnow.android.ui.base.BaseInputBuilder
import com.example.inhabitnow.android.ui.base.BaseTextFiledBuilder

@Composable
fun BaseCreateEditTagDialog(
    isCreate: Boolean,
    state: BaseCreateEditTagScreenState,
    onEvent: (BaseCreateEditTagScreenEvent) -> Unit
) {
    BaseDialogBuilder.BaseDialog(
        onDismissRequest = { onEvent(BaseCreateEditTagScreenEvent.OnDismissRequest) },
        title = {
            BaseDialogBuilder.BaseDialogTitle(
                titleText = if (isCreate) "Create tag" else "Edit tag"
            )
        },
        actionButtons = BaseDialogBuilder.ActionButtons(
            confirmButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Confirm",
                    enabled = state.canConfirm,
                    onClick = {
                        onEvent(BaseCreateEditTagScreenEvent.OnConfirmClick)
                    }
                )
            },
            dismissButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Cancel",
                    onClick = {
                        onEvent(BaseCreateEditTagScreenEvent.OnDismissRequest)
                    }
                )
            }
        )
    ) {
        val focusRequester = remember {
            FocusRequester()
        }
        BaseTextFiledBuilder.BaseOutlinedTextField(
            value = state.inputTitle,
            onValueChange = {
                onEvent(BaseCreateEditTagScreenEvent.OnInputUpdateTitle(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            label = {
                Text(text = "name")
            },
            singleLine = true
        )
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}