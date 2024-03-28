package com.example.inhabitnow.android.presentation.view_schedule.config.enter_number_record

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_number_record.components.EnterTaskNumberRecordScreenEvent
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_number_record.components.EnterTaskNumberRecordScreenResult
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_number_record.components.EnterTaskNumberRecordScreenState
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder
import com.example.inhabitnow.android.ui.base.BaseTextFiledBuilder
import com.example.inhabitnow.android.ui.limitNumberToString
import com.example.inhabitnow.android.ui.toDisplay
import com.example.inhabitnow.android.ui.toShortMonthDayYear

@Composable
fun EnterTaskNumberRecordDialog(
    stateHolder: EnterTaskNumberRecordStateHolder,
    onResult: (EnterTaskNumberRecordScreenResult) -> Unit
) {
    BaseScreen(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        EnterTaskNumberRecordDialogStateless(state, onEvent)
    }
}

@Composable
private fun EnterTaskNumberRecordDialogStateless(
    state: EnterTaskNumberRecordScreenState,
    onEvent: (EnterTaskNumberRecordScreenEvent) -> Unit
) {
    BaseDialogBuilder.BaseDialog(
        onDismissRequest = { onEvent(EnterTaskNumberRecordScreenEvent.OnDismissRequest) },
        actionButtons = BaseDialogBuilder.ActionButtons(
            confirmButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Confirm",
                    enabled = state.canConfirm,
                    onClick = {
                        onEvent(EnterTaskNumberRecordScreenEvent.OnConfirmClick)
                    }
                )
            },
            dismissButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Cancel",
                    onClick = {
                        onEvent(EnterTaskNumberRecordScreenEvent.OnDismissRequest)
                    }
                )
            }
        )
    ) {
        Text(
            text = state.task.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = state.date.toShortMonthDayYear(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        val focusRequester = remember { FocusRequester() }
        BaseTextFiledBuilder.BaseOutlinedTextField(
            value = state.inputNumber,
            onValueChange = { onEvent(EnterTaskNumberRecordScreenEvent.InputUpdateNumber(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            valueValidator = state.inputValidator,
            label = {
                Text(text = "number")
            },
//            supportingText = {
//                val goalText = remember {
//                    state.task.progressContent.let { pc ->
//                        val limitNumber = pc.limitNumber.limitNumberToString()
//                        val limitType = pc.limitType.toDisplay()
//                        "$limitType $limitNumber ${pc.limitUnit}"
//                    }
//                }
//                Text(
//                    text = "Goal: $goalText",
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        val goalText = remember {
            state.task.progressContent.let { pc ->
                val limitNumber = pc.limitNumber.limitNumberToString()
                val limitType = pc.limitType.toDisplay()
                "$limitType $limitNumber ${pc.limitUnit}"
            }
        }
        Text(
            text = "Goal: $goalText",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 4.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}