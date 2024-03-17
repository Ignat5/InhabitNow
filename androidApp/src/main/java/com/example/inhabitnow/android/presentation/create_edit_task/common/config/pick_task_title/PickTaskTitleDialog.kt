package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
                BaseDialogBuilder.BaseActionButton(
                    text = "Confirm",
                    enabled = state.canConfirm,
                    onClick = { onEvent(PickTaskTitleScreenEvent.OnConfirmClick) }
                )
            },
            dismissButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Cancel",
                    onClick = { onEvent(PickTaskTitleScreenEvent.OnDismissRequest) }
                )
            }
        )
    ) {
        val focusRequester = remember { FocusRequester() }
        BaseTextFiledBuilder.BaseOutlinedTextField(
            value = state.inputTitle,
            onValueChange = {
                onEvent(PickTaskTitleScreenEvent.OnInputUpdate(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            singleLine = true,
            label = {
                Text(text = "name")
            }
        )

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}