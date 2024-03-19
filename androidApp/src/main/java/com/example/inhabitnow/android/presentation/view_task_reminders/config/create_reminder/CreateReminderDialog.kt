package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder

import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.inhabitnow.android.R
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
import com.example.inhabitnow.android.ui.toIconResId
import com.example.inhabitnow.core.type.ReminderType
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek

@Composable
fun CreateReminderDialog(
    stateHolder: CreateReminderStateHolder,
    onResult: (CreateReminderScreenResult) -> Unit
) {
    BaseScreen(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        CreateReminderDialogStateless(state, onEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        val allReminderTypes = remember {
            ReminderType.entries
        }
        val allScheduleTypes = remember {
            UIReminderContent.Schedule.Type.entries
        }
        val coroutineScope = rememberCoroutineScope()
        val lazyListState = rememberLazyListState()
        val screenHeight = LocalConfiguration.current.screenHeightDp
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp),
        ) {

            label(
                text = "Reminder time",
                textAlign = TextAlign.Center,
                modifier = Modifier
            )

            item(
                key = ScreenItemKey.Time.name,
                contentType = ScreenItemKey.Time.name
            ) {
                val initHours = remember { state.hours }
                val initMinutes = remember { state.minutes }

                BaseTimePicker(
                    initHours = initHours,
                    initMinutes = initMinutes,
                    onHoursChanged = {
                        onEvent(CreateReminderScreenEvent.OnHoursValueUpdate(it))
                    },
                    onMinutesChanged = {
                        onEvent(CreateReminderScreenEvent.OnMinutesValueUpdate(it))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            spacer()

            label(
                text = "Reminder type",
                textAlign = TextAlign.Start,
                modifier = Modifier
            )

            itemsReminderType(
                allReminderTypes = allReminderTypes,
                currentReminderType = state.type,
                onReminderTypeClick = {
                    onEvent(CreateReminderScreenEvent.OnPickReminderType(it))
                }
            )

            spacer()

            label(
                text = "Reminder schedule",
                textAlign = TextAlign.Start,
                modifier = Modifier
            )

            itemsReminderSchedule(
                allScheduleTypes = allScheduleTypes,
                currentScheduleContent = state.schedule,
                onScheduleTypeClick = { type ->
                    onEvent(CreateReminderScreenEvent.OnReminderScheduleTypeClick(type))
                },
                onDayOfWeekClick = {
                    onEvent(CreateReminderScreenEvent.OnDayOfWeekClick(it))
                }
            )
        }

        LaunchedEffect(state.schedule.type) {
            val shouldScrollToBottom = lazyListState.canScrollForward &&
                    state.schedule.type == UIReminderContent.Schedule.Type.DaysOfWeek
            if (shouldScrollToBottom) {
                coroutineScope.launch {
                    lazyListState.animateScrollBy(screenHeight.toFloat())
                }
            }
        }

    }
}

private fun LazyListScope.itemsReminderType(
    allReminderTypes: List<ReminderType>,
    currentReminderType: ReminderType,
    onReminderTypeClick: (ReminderType) -> Unit
) {
    items(
        items = allReminderTypes,
        key = { it.name },
        contentType = { ScreenItemKey.ItemRadioButton }
    ) { item ->
        val titleText = remember {
            item.toDisplay()
        }
        val isSelected = remember(currentReminderType) {
            item == currentReminderType
        }
        BaseItemOptionBuilder.BaseItemRadioButton(
            titleText = titleText,
            isSelected = isSelected,
            onClick = {
                onReminderTypeClick(item)
            }
        )
    }
}

private fun LazyListScope.itemsReminderSchedule(
    allScheduleTypes: List<UIReminderContent.Schedule.Type>,
    currentScheduleContent: UIReminderContent.Schedule,
    onScheduleTypeClick: (UIReminderContent.Schedule.Type) -> Unit,
    onDayOfWeekClick: (DayOfWeek) -> Unit
) {
    items(
        items = allScheduleTypes,
        key = { it.name },
        contentType = { ScreenItemKey.ItemRadioButton.name }
    ) { item ->
        val isSelected = remember(currentScheduleContent.type) {
            item == currentScheduleContent.type
        }
        val onClick = remember {
            val callback: () -> Unit = {
                onScheduleTypeClick(item)
            }
            callback
        }
        when (item) {
            UIReminderContent.Schedule.Type.EveryDay -> {
                BaseItemOptionBuilder.BaseItemRadioButton(
                    titleText = "Always enabled",
                    isSelected = isSelected,
                    onClick = onClick
                )
            }

            UIReminderContent.Schedule.Type.DaysOfWeek -> {
                Column(modifier = Modifier.fillMaxWidth()) {
                    BaseItemOptionBuilder.BaseItemRadioButton(
                        titleText = "Specific days of week",
                        isSelected = isSelected,
                        onClick = onClick
                    )
                    if (isSelected) {
                        (currentScheduleContent as? UIReminderContent.Schedule.DaysOfWeek)?.let { sc ->
                            Spacer(modifier = Modifier.height(8.dp))
                            BaseDaysOfWeekInput(
                                selectedDaysOfWeek = sc.daysOfWeek,
                                onDayOfWeekClick = {
                                    onDayOfWeekClick(it)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun LazyListScope.label(
    text: String,
    textAlign: TextAlign,
    modifier: Modifier = Modifier
) {
    item(
        key = null,
        contentType = ScreenItemKey.Label,
    ) {
        Column(modifier = modifier.fillMaxWidth()) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = textAlign,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

private fun LazyListScope.spacer() {
    item(
        key = null,
        contentType = ScreenItemKey.Spacer.name
    ) {
        Spacer(modifier = Modifier.height(16.dp))
    }
}

private enum class ScreenItemKey {
    Time,
    ItemRadioButton,
    Label,
    Spacer
}