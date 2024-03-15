package com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.time

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.time.components.PickTaskTimeProgressScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.time.components.PickTaskTimeProgressScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.time.components.PickTaskTimeProgressScreenState
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder
import com.example.inhabitnow.android.ui.base.BaseInputBuilder
import com.example.inhabitnow.android.ui.toDisplay
import com.example.inhabitnow.core.type.ProgressLimitType

@Composable
fun PickTaskTimeProgressDialog(
    stateHolder: PickTaskTimeProgressStateHolder,
    onResult: (PickTaskTimeProgressScreenResult) -> Unit
) {
    BaseScreen(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        PickTaskTimeProgressDialogStateless(state, onEvent)
    }
}

@Composable
private fun PickTaskTimeProgressDialogStateless(
    state: PickTaskTimeProgressScreenState,
    onEvent: (PickTaskTimeProgressScreenEvent) -> Unit
) {
    BaseDialogBuilder.BaseDialog(
        onDismissRequest = { onEvent(PickTaskTimeProgressScreenEvent.OnDismissRequest) },
        title = {
            BaseDialogBuilder.BaseDialogTitle(titleText = "Daily goal")
        },
        actionButtons = BaseDialogBuilder.ActionButtons(
            confirmButton = {
                BaseDialogBuilder.ActionButton(
                    text = "Confirm",
                    onClick = {

                    }
                )
            },
            dismissButton = {
                BaseDialogBuilder.ActionButton(
                    text = "Cancel",
                    onClick = {

                    }
                )
            }
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BaseInputBuilder.BaseOutlinedInputDropdown(
                allOptions = ProgressLimitType.entries,
                currentOption = state.limitType,
                optionText = { it.toDisplay() },
                onOptionClick = {
                    onEvent(PickTaskTimeProgressScreenEvent.OnPickLimitType(it))
                }
            )
        }
    }
}