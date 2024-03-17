package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.number

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.number.components.PickTaskNumberProgressScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.number.components.PickTaskNumberProgressScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.number.components.PickTaskNumberProgressScreenState
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder
import com.example.inhabitnow.android.ui.base.BaseInputBuilder
import com.example.inhabitnow.android.ui.base.BaseTextFiledBuilder
import com.example.inhabitnow.android.ui.toDisplay
import com.example.inhabitnow.core.type.ProgressLimitType

@Composable
fun PickTaskNumberProgressDialog(
    stateHolder: PickTaskNumberProgressStateHolder,
    onResult: (PickTaskNumberProgressScreenResult) -> Unit
) {
    BaseScreen(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        PickTaskNumberProgressDialogStateless(state, onEvent)
    }
}

@Composable
private fun PickTaskNumberProgressDialogStateless(
    state: PickTaskNumberProgressScreenState,
    onEvent: (PickTaskNumberProgressScreenEvent) -> Unit
) {
    BaseDialogBuilder.BaseDialog(
        onDismissRequest = { onEvent(PickTaskNumberProgressScreenEvent.OnDismissRequest) },
        properties = DialogProperties(dismissOnClickOutside = false),
        title = {
            BaseDialogBuilder.BaseDialogTitle(titleText = "Daily goal")
        },
        actionButtons = BaseDialogBuilder.ActionButtons(
            confirmButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Confirm",
                    enabled = state.canSave,
                    onClick = {
                        onEvent(PickTaskNumberProgressScreenEvent.OnConfirmClick)
                    }
                )
            },
            dismissButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Cancel",
                    onClick = {
                        onEvent(PickTaskNumberProgressScreenEvent.OnDismissRequest)
                    }
                )
            }
        )
    ) {
        val focusManager = LocalFocusManager.current
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BaseInputBuilder.BaseOutlinedInputDropdown(
                allOptions = ProgressLimitType.entries,
                currentOption = state.limitType,
                optionText = { it.toDisplay() },
                onOptionClick = {
                    onEvent(PickTaskNumberProgressScreenEvent.OnPickLimitType(it))
                },
                modifier = Modifier.fillMaxWidth()
            )
            BaseTextFiledBuilder.BaseOutlinedTextField(
                value = state.limitNumber,
                onValueChange = {
                    onEvent(PickTaskNumberProgressScreenEvent.OnInputUpdateLimitNumber(it))
                },
                valueValidator = state.limitNumberValidator,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Goal")
                },
                singleLine = true,
                keyboardActions = KeyboardActions {
                    focusManager.moveFocus(FocusDirection.Next)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BaseTextFiledBuilder.BaseOutlinedTextField(
                    value = state.limitUnit,
                    onValueChange = {
                        onEvent(PickTaskNumberProgressScreenEvent.OnInputUpdateLimitUnit(it))
                    },
                    modifier = Modifier.weight(1f),
                    label = {
                        Text(text = "Unit")
                    },
                    keyboardActions = KeyboardActions {
                        focusManager.clearFocus()
                    },
                    singleLine = true
                )
                Text(
                    text = "a day",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}