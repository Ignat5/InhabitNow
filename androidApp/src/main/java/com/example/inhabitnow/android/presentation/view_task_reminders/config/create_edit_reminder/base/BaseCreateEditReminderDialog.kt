package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.base

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.base.components.BaseCreateEditReminderScreenEvent
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.base.components.BaseCreateEditReminderScreenState
import com.example.inhabitnow.android.ui.base.BaseDaysOfWeekInput
import com.example.inhabitnow.android.ui.base.BaseDialogBuilder
import com.example.inhabitnow.android.ui.base.BaseItemOptionBuilder
import com.example.inhabitnow.android.ui.base.BaseTimePicker
import com.example.inhabitnow.android.ui.toDisplay
import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.domain.model.reminder.content.ReminderContentModel
import kotlinx.datetime.DayOfWeek

private const val SCREEN_FRACTION = 0.75f

@Composable
fun BaseCreateEditReminderDialog(
    isCreate: Boolean,
    state: BaseCreateEditReminderScreenState,
    onEvent: (BaseCreateEditReminderScreenEvent) -> Unit
) {
    BaseDialogBuilder.BaseStaticDialog(
        onDismissRequest = { onEvent(BaseCreateEditReminderScreenEvent.OnDismissRequest) },
        properties = DialogProperties(dismissOnClickOutside = false),
        screenFraction = SCREEN_FRACTION,
        title = {
            BaseDialogBuilder.BaseDialogTitle(
                titleText = if (isCreate) "Create reminder"
                else "Edit reminder"
            )
        },
        actionButtons = BaseDialogBuilder.ActionButtons(
            confirmButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Confirm",
                    enabled = state.canConfirm,
                    onClick = {
                        onEvent(BaseCreateEditReminderScreenEvent.OnConfirmClick)
                    }
                )
            },
            dismissButton = {
                BaseDialogBuilder.BaseActionButton(
                    text = "Cancel",
                    onClick = {
                        onEvent(BaseCreateEditReminderScreenEvent.OnDismissRequest)
                    }
                )
            }
        )
    ) {
        val allReminderTypes = remember {
            ReminderType.entries
        }
        val allScheduleTypes = remember {
            ReminderContentModel.ScheduleContent.Type.entries
        }
        val lazyListState = rememberLazyListState()

        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp),
        ) {
            label(
                text = "Reminder time",
                textAlign = TextAlign.Center,
            )
            itemPickTime(
                initHours = state.hours,
                initMinutes = state.minutes,
                onHoursChanged = {
                    onEvent(BaseCreateEditReminderScreenEvent.OnHoursValueUpdate(it))
                },
                onMinutesChanged = {
                    onEvent(BaseCreateEditReminderScreenEvent.OnMinutesValueUpdate(it))
                }
            )
            spacer()
            label(
                text = "Reminder type",
                textAlign = TextAlign.Start,
            )
            itemsReminderType(
                allReminderTypes = allReminderTypes,
                currentReminderType = state.type,
                onReminderTypeClick = {
                    onEvent(BaseCreateEditReminderScreenEvent.OnReminderTypeClick(it))
                }
            )
            spacer()
            label(
                text = "Reminder schedule",
                textAlign = TextAlign.Start,
            )
            itemsReminderSchedule(
                allScheduleTypes = allScheduleTypes,
                currentScheduleContent = state.schedule,
                onScheduleTypeClick = {
                    onEvent(BaseCreateEditReminderScreenEvent.OnReminderScheduleTypeClick(it))
                },
                onDayOfWeekClick = {
                    onEvent(BaseCreateEditReminderScreenEvent.OnDayOfWeekClick(it))
                }
            )


        }
    }
}

private fun LazyListScope.itemPickTime(
    initHours: Int,
    initMinutes: Int,
    onHoursChanged: (Int) -> Unit,
    onMinutesChanged: (Int) -> Unit,
) {
    item(
        key = ScreenItemContentType.Time,
        contentType = ScreenItemContentType.Time
    ) {
        BaseTimePicker(
            initHours = initHours,
            initMinutes = initMinutes,
            onHoursChanged = onHoursChanged,
            onMinutesChanged = onMinutesChanged,
            modifier = Modifier.fillMaxWidth()
        )
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
        contentType = { ScreenItemContentType.ItemRadioButton }
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
    allScheduleTypes: List<ReminderContentModel.ScheduleContent.Type>,
    currentScheduleContent: ReminderContentModel.ScheduleContent,
    onScheduleTypeClick: (ReminderContentModel.ScheduleContent.Type) -> Unit,
    onDayOfWeekClick: (DayOfWeek) -> Unit
) {
    items(
        items = allScheduleTypes,
        key = { it.name },
        contentType = { ScreenItemContentType.ItemRadioButton }
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
            ReminderContentModel.ScheduleContent.Type.EveryDay -> {
                BaseItemOptionBuilder.BaseItemRadioButton(
                    titleText = "Always enabled",
                    isSelected = isSelected,
                    onClick = onClick
                )
            }

            ReminderContentModel.ScheduleContent.Type.DaysOfWeek -> {
                Column(modifier = Modifier.fillMaxWidth()) {
                    BaseItemOptionBuilder.BaseItemRadioButton(
                        titleText = "Specific days of week",
                        isSelected = isSelected,
                        onClick = onClick
                    )
                    if (isSelected) {
                        (currentScheduleContent as? ReminderContentModel.ScheduleContent.DaysOfWeek)?.let { sc ->
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
        contentType = ScreenItemContentType.Label,
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
        contentType = ScreenItemContentType.Spacer
    ) {
        Spacer(modifier = Modifier.height(16.dp))
    }
}


private enum class ScreenItemContentType {
    Time,
    ItemRadioButton,
    Label,
    Spacer
}