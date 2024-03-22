package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_priority

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_priority.components.PickTaskPriorityScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_priority.components.PickTaskPriorityScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_priority.components.PickTaskPriorityScreenState
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder
import com.example.inhabitnow.android.ui.base.BaseTextFiledBuilder

@Composable
fun PickTaskPriorityDialog(
    stateHolder: PickTaskPriorityStateHolder,
    onResult: (PickTaskPriorityScreenResult) -> Unit
) {
    BaseScreen(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        PickTaskPriorityDialogStateless(state, onEvent)
    }
}

@Composable
private fun PickTaskPriorityDialogStateless(
    state: PickTaskPriorityScreenState,
    onEvent: (PickTaskPriorityScreenEvent) -> Unit
) {
    BaseDialogBuilder.BaseDialog(
        onDismissRequest = { onEvent(PickTaskPriorityScreenEvent.OnDismissRequest) },
        actionButtons = BaseDialogBuilder.ActionButtons(
            confirmButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Confirm",
                    enabled = state.canConfirm,
                    onClick = {
                        onEvent(PickTaskPriorityScreenEvent.OnConfirmClick)
                    }
                )
            },
            dismissButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Cancel",
                    onClick = {
                        onEvent(PickTaskPriorityScreenEvent.OnDismissRequest)
                    }
                )
            }
        )
    ) {
        val focusRequester = remember { FocusRequester() }
        BaseTextFiledBuilder.BaseOutlinedTextField(
            value = state.inputPriority,
            onValueChange = { onEvent(PickTaskPriorityScreenEvent.OnInputUpdatePriority(it)) },
            valueValidator = state.valueValidator,
            singleLine = true,
            label = {
                Text(text = "priority")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
        )
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}