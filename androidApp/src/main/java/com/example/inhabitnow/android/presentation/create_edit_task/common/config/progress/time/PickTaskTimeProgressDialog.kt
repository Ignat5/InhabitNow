package com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.time

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.time.components.PickTaskTimeProgressScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.time.components.PickTaskTimeProgressScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.time.components.PickTaskTimeProgressScreenState
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder
import com.example.inhabitnow.android.ui.base.BaseInputBuilder
import com.example.inhabitnow.android.ui.base.BaseTimeInput
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
        properties = DialogProperties(dismissOnClickOutside = false),
        title = {
            BaseDialogBuilder.BaseDialogTitle(titleText = "Daily goal")
        },
        actionButtons = BaseDialogBuilder.ActionButtons(
            confirmButton = {
                BaseDialogBuilder.ActionButton(
                    text = "Confirm",
                    onClick = {
                        onEvent(PickTaskTimeProgressScreenEvent.OnConfirmClick)
                    }
                )
            },
            dismissButton = {
                BaseDialogBuilder.ActionButton(
                    text = "Cancel",
                    onClick = {
                        onEvent(PickTaskTimeProgressScreenEvent.OnDismissRequest)
                    }
                )
            }
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BaseInputBuilder.BaseOutlinedInputDropdown(
                allOptions = ProgressLimitType.entries,
                currentOption = state.limitType,
                optionText = { it.toDisplay() },
                onOptionClick = {
                    onEvent(PickTaskTimeProgressScreenEvent.OnPickLimitType(it))
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BaseTimeInput(
                    hours = state.limitHours,
                    minutes = state.limitMinutes,
                    onInputUpdateHours = {
                        onEvent(PickTaskTimeProgressScreenEvent.OnInputUpdateHours(it))
                    },
                    onInputUpdateMinutes = {
                        onEvent(PickTaskTimeProgressScreenEvent.OnInputUpdateMinutes(it))
                    },
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "a day",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}