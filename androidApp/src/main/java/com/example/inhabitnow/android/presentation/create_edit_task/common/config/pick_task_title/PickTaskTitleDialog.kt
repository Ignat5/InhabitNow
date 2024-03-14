package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.components.PickTaskTitleScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.components.PickTaskTitleScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.components.PickTaskTitleScreenState
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder
import com.example.inhabitnow.android.ui.base.BaseTextFiledBuilder

@Composable
fun PickTaskTitleDialog(
    stateHolder: PickTaskTitleStateHolder,
    onResult: (PickTaskTitleScreenResult) -> Unit
) {
    BaseScreen(
        stateHolder = stateHolder,
        onResult = onResult
    ) { state, onEvent ->
        PickTaskTitleDialogStateless(state, onEvent)
    }
}

@Composable
private fun PickTaskTitleDialogStateless(
    state: PickTaskTitleScreenState,
    onEvent: (PickTaskTitleScreenEvent) -> Unit
) {
    BaseDialogBuilder.BaseDialog(
        onDismissRequest = { onEvent(PickTaskTitleScreenEvent.OnDismissRequest) },
        actionButtons = BaseDialogBuilder.ActionButtons(
            confirmButton = {
                BaseDialogBuilder.ActionButton(
                    text = "Confirm",
                    enabled = state.canConfirm,
                    onClick = { onEvent(PickTaskTitleScreenEvent.OnConfirmClick) }
                )
            },
            dismissButton = {
                BaseDialogBuilder.ActionButton(
                    text = "Cancel",
                    onClick = { onEvent(PickTaskTitleScreenEvent.OnDismissRequest) }
                )
            }
        )
    ) {
        BaseTextFiledBuilder.BaseOutlinedTextField(
            text = state.inputTitle,
            onValueChange = {
                onEvent(PickTaskTitleScreenEvent.OnInputUpdate(it))
            },
            modifier = Modifier.fillMaxWidth(),
            isInitFocused = true,
            singleLine = true,
            label = {
                Text(text = "name")
            },
        )
    }
}