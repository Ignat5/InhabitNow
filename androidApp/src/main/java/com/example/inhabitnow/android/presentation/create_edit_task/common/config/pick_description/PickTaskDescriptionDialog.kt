package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_description

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_description.components.PickTaskDescriptionScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_description.components.PickTaskDescriptionScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_description.components.PickTaskDescriptionScreenState
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder
import com.example.inhabitnow.android.ui.base.BaseTextFiledBuilder

@Composable
fun PickTaskDescriptionDialog(
    stateHolder: PickTaskDescriptionStateHolder,
    onResult: (PickTaskDescriptionScreenResult) -> Unit
) {
    BaseScreen(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        PickTaskDescriptionDialogStateless(state, onEvent)
    }
}

@Composable
private fun PickTaskDescriptionDialogStateless(
    state: PickTaskDescriptionScreenState,
    onEvent: (PickTaskDescriptionScreenEvent) -> Unit
) {
    BaseDialogBuilder.BaseDialog(
        onDismissRequest = { onEvent(PickTaskDescriptionScreenEvent.OnDismissRequest) },
        actionButtons = BaseDialogBuilder.ActionButtons(
            confirmButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Confirm",
                    onClick = {
                        onEvent(PickTaskDescriptionScreenEvent.OnConfirmClick)
                    }
                )
            },
            dismissButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Cancel",
                    onClick = {
                        onEvent(PickTaskDescriptionScreenEvent.OnDismissRequest)
                    }
                )
            }
        )
    ) {
        val focusRequester = remember { FocusRequester() }
        BaseTextFiledBuilder.BaseOutlinedTextField(
            value = state.inputDescription,
            onValueChange = { onEvent(PickTaskDescriptionScreenEvent.OnInputUpdateDescription(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            minLines = 2,
            label = {
                Text(text = "description")
            }
        )
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}