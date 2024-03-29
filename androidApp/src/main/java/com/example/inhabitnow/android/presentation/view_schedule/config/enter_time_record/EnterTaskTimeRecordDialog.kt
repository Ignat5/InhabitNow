package com.example.inhabitnow.android.presentation.view_schedule.config.enter_time_record

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
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_time_record.components.EnterTaskTimeRecordScreenEvent
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_time_record.components.EnterTaskTimeRecordScreenResult
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_time_record.components.EnterTaskTimeRecordScreenState
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder
import com.example.inhabitnow.android.ui.base.BaseTextFiledBuilder
import com.example.inhabitnow.android.ui.base.BaseTimeInput
import com.example.inhabitnow.android.ui.limitNumberToString
import com.example.inhabitnow.android.ui.toDisplay
import com.example.inhabitnow.android.ui.toHourMinute
import com.example.inhabitnow.android.ui.toShortMonthDayYear

@Composable
fun EnterTaskTimeRecordDialog(
    stateHolder: EnterTaskTimeRecordStateHolder,
    onResult: (EnterTaskTimeRecordScreenResult) -> Unit
) {
    BaseScreen(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        EnterTaskTimeRecordDialogStateless(state, onEvent)
    }
}

@Composable
private fun EnterTaskTimeRecordDialogStateless(
    state: EnterTaskTimeRecordScreenState,
    onEvent: (EnterTaskTimeRecordScreenEvent) -> Unit
) {
    BaseDialogBuilder.BaseDialog(
        onDismissRequest = { onEvent(EnterTaskTimeRecordScreenEvent.OnDismissRequest) },
        actionButtons = BaseDialogBuilder.ActionButtons(
            confirmButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Confirm",
                    onClick = {
                        onEvent(EnterTaskTimeRecordScreenEvent.OnConfirmClick)
                    }
                )
            },
            dismissButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Cancel",
                    onClick = {
                        onEvent(EnterTaskTimeRecordScreenEvent.OnDismissRequest)
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
        Spacer(modifier = Modifier.height(16.dp))

        BaseTimeInput(
            hours = state.inputHours,
            minutes = state.inputMinutes,
            onInputUpdateHours = { onEvent(EnterTaskTimeRecordScreenEvent.OnInputUpdateHours(it)) },
            onInputUpdateMinutes = { onEvent(EnterTaskTimeRecordScreenEvent.OnInputUpdateMinutes(it)) },
            modifier = Modifier
                .fillMaxWidth()
        )

        val goalText = remember {
            state.task.progressContent.let { pc ->
                val limitTime = pc.limitTime.toHourMinute()
                val limitType = pc.limitType.toDisplay()
                "$limitType $limitTime"
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Goal: $goalText",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}