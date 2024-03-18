package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.config.pick_time

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.DialogProperties
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder
import com.example.inhabitnow.android.ui.base.BaseTimePicker

@Composable
fun PickTimeDialog(
    initHours: Int,
    initMinutes: Int,
    onResult: (PickTimeScreenResult) -> Unit
) {
    var currentHoursValue by rememberSaveable {
        mutableIntStateOf(initHours)
    }

    var currentMinutesValue by rememberSaveable {
        mutableIntStateOf(initMinutes)
    }

    BaseDialogBuilder.BaseDialog(
        onDismissRequest = { onResult(PickTimeScreenResult.Dismiss) },
        properties = DialogProperties(dismissOnClickOutside = false),
        actionButtons = BaseDialogBuilder.ActionButtons(
            confirmButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Confirm",
                    onClick = {
                        onResult(
                            PickTimeScreenResult.Confirm(
                                hours = currentHoursValue,
                                minutes = currentMinutesValue
                            )
                        )
                    }
                )
            },
            dismissButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Cancel",
                    onClick = {
                        onResult(PickTimeScreenResult.Dismiss)
                    }
                )
            }
        )
    ) {
        BaseTimePicker(
            initHours = initHours,
            initMinutes = initMinutes,
            onHoursChanged = { currentHoursValue = it },
            onMinutesChanged = { currentMinutesValue = it }
        )
    }
}