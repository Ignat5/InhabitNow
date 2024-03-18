package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.inhabitnow.android.presentation.base.components.config.BaseConfigState
import com.example.inhabitnow.android.presentation.base.ext.BaseScreen
import com.example.inhabitnow.android.presentation.model.UIReminderContent
import com.example.inhabitnow.android.presentation.model.UITaskContent
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.components.CreateReminderScreenEvent
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.components.CreateReminderScreenResult
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.components.CreateReminderScreenState
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.config.CreateReminderScreenConfig
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.config.pick_time.PickTimeDialog
import com.example.inhabitnow.android.ui.base.BaseDaysOfWeekInput
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder
import com.example.inhabitnow.android.ui.base.BaseInputBuilder
import com.example.inhabitnow.android.ui.base.BaseItemOptionBuilder
import com.example.inhabitnow.android.ui.base.BaseTimeInput
import com.example.inhabitnow.android.ui.base.BaseTimePicker
import com.example.inhabitnow.android.ui.toDisplay
import com.example.inhabitnow.android.ui.toHourMinute
import com.example.inhabitnow.core.type.ReminderType

@Composable
fun CreateReminderDialog(
    stateHolder: CreateReminderStateHolder,
    onResult: (CreateReminderScreenResult) -> Unit
) {
    BaseScreen(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        CreateReminderDialogStateless(state, onEvent)
    }
}

@Composable
private fun CreateReminderDialogStateless(
    state: CreateReminderScreenState,
    onEvent: (CreateReminderScreenEvent) -> Unit
) {
    BaseDialogBuilder.BaseStaticDialog(
        onDismissRequest = { onEvent(CreateReminderScreenEvent.OnDismissRequest) },
        properties = DialogProperties(dismissOnClickOutside = false),
        screenFraction = 0.75f,
        title = {
            BaseDialogBuilder.BaseDialogTitle(titleText = "Create reminder")
        },
        actionButtons = BaseDialogBuilder.ActionButtons(
            confirmButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Confirm",
                    enabled = state.canConfirm,
                    onClick = { onEvent(CreateReminderScreenEvent.OnConfirmClick) }
                )
            },
            dismissButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Cancel",
                    onClick = { onEvent(CreateReminderScreenEvent.OnDismissRequest) }
                )
            }
        )
    ) {
        val allScheduleTypes = remember {
            UIReminderContent.Schedule.Type.entries
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp),
        ) {

            item(
                key = ScreenItemKey.Type.name,
                contentType = ScreenItemKey.Type.name
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    BaseInputBuilder.BaseOutlinedInputDropdown(
                        allOptions = ReminderType.entries,
                        currentOption = state.type,
                        optionText = { it.toDisplay() },
                        onOptionClick = {
                            onEvent(CreateReminderScreenEvent.OnPickReminderType(it))
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            item {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item(
                key = ScreenItemKey.Time.name,
                contentType = ScreenItemKey.Time.name
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    val initHours = remember { state.time.hour }
                    val initMinutes = remember { state.time.minute }

                    BaseTimePicker(
                        initHours = initHours,
                        initMinutes = initMinutes,
                        onHoursChanged = {},
                        onMinutesChanged = {},
                        modifier = Modifier.fillMaxWidth()
                    )

//                    BaseTimeInput(
//                        hours = initHours,
//                        minutes = initMinutes,
//                        onInputUpdateHours = {},
//                        onInputUpdateMinutes = {} ,
//                        modifier = Modifier.fillMaxWidth()
//                    )


//                    BaseInputBuilder.BaseOutlinedInputBox(
//                        onClick = { onEvent(CreateReminderScreenEvent.OnPickReminderTimeClick) },
//                    ) {
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.Center
//                        ) {
//                            Text(
//                                text = state.time.toHourMinute(),
//                                style = MaterialTheme.typography.bodyLarge,
//                                color = MaterialTheme.colorScheme.onSurface,
//                                modifier = Modifier.fillMaxWidth(),
//                                textAlign = TextAlign.Center
//                            )
//                        }
//                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            item {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            items(
                items = allScheduleTypes,
                key = { item -> item.name },
                contentType = { ScreenItemKey.ScheduleItem.ordinal }
            ) { item ->
                val isSelected = item == state.schedule.type
                val onClick = remember {
                    val callback: () -> Unit = {
                        onEvent(CreateReminderScreenEvent.OnReminderScheduleTypeClick(item))
                    }
                    callback
                }
                when (item) {
                    UIReminderContent.Schedule.Type.EveryDay -> {
                        ItemReminderType(
                            titleText = "Always enabled",
                            isSelected = isSelected,
                            onClick = onClick
                        )
                    }

                    UIReminderContent.Schedule.Type.DaysOfWeek -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            ItemReminderType(
                                titleText = "Specific days of week",
                                isSelected = isSelected,
                                onClick = onClick
                            )
                            if (isSelected) {
                                (state.schedule as? UIReminderContent.Schedule.DaysOfWeek)?.let { sc ->
                                    Spacer(modifier = Modifier.height(8.dp))
                                    BaseDaysOfWeekInput(
                                        selectedDaysOfWeek = sc.daysOfWeek,
                                        onDayOfWeekClick = {
                                            onEvent(CreateReminderScreenEvent.OnDayOfWeekClick(it))
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        when (val baseConfig = state.baseConfig) {
            is BaseConfigState.Idle -> Unit
            is BaseConfigState.Config -> {
                when (val config = baseConfig.config) {
                    is CreateReminderScreenConfig.PickTime -> {
                        PickTimeDialog(
                            initHours = config.time.hour,
                            initMinutes = config.time.minute,
                            onResult = {

                            }
                        )
                    }
                }
            }
        }

    }
}

@Composable
private fun ItemReminderType(
    titleText: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    BaseItemOptionBuilder.BaseItemRadioButton(
        titleText = titleText,
        isSelected = isSelected,
        onClick = onClick
    )
}

private enum class ScreenItemKey {
    Time,
    Type,
    ScheduleItem
}