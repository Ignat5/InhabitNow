package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.create

import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.base.BaseCreateEditReminderStateHolder
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.create.components.CreateReminderScreenEvent
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.create.components.CreateReminderScreenResult
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.create.components.CreateReminderScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalTime

class CreateReminderStateHolder(
    holderScope: CoroutineScope
) : BaseCreateEditReminderStateHolder<CreateReminderScreenEvent, CreateReminderScreenState, CreateReminderScreenResult>(
    holderScope = holderScope
) {

    override val uiScreenState: StateFlow<CreateReminderScreenState> =
        combine(
            inputHoursState, inputMinutesState, inputTypeState, inputScheduleState, canConfirmState
        ) { inputHours, inputMinutes, inputType, inputSchedule, canConfirm ->
            CreateReminderScreenState(
                hours = inputHours,
                minutes = inputMinutes,
                type = inputType,
                schedule = inputSchedule,
                canConfirm = canConfirm
            )
        }.stateIn(
            holderScope,
            SharingStarted.WhileSubscribed(5000L),
            CreateReminderScreenState(
                hours = inputHoursState.value,
                minutes = inputMinutesState.value,
                type = inputTypeState.value,
                schedule = inputScheduleState.value,
                canConfirm = canConfirmState.value
            )
        )

    override fun onEvent(event: CreateReminderScreenEvent) {
        when (event) {
            is CreateReminderScreenEvent.BaseEvent -> onBaseEvent(event.baseEvent)
        }
    }

    override fun onConfirm() {
        setUpResult(
            CreateReminderScreenResult.Confirm(
                time = LocalTime(hour = inputHoursState.value, minute = inputMinutesState.value),
                type = inputTypeState.value,
                uiScheduleContent = inputScheduleState.value
            )
        )
    }

    override fun onDismiss() {
        setUpResult(CreateReminderScreenResult.Dismiss)
    }


}