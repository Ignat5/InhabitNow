package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.edit

import com.example.inhabitnow.android.presentation.model.UIReminderContent
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.base.BaseCreateEditReminderStateHolder
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.edit.components.EditReminderScreenEvent
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.edit.components.EditReminderScreenResult
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.edit.components.EditReminderScreenState
import com.example.inhabitnow.android.ui.toUIScheduleContent
import com.example.inhabitnow.core.type.ReminderType
import com.example.inhabitnow.domain.model.reminder.ReminderModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalTime

class EditReminderStateHolder(
    private val reminderId: String,
    reminderTime: LocalTime,
    reminderType: ReminderType,
    reminderSchedule: UIReminderContent.Schedule,
    holderScope: CoroutineScope
) : BaseCreateEditReminderStateHolder<EditReminderScreenEvent, EditReminderScreenState, EditReminderScreenResult>(
    initTime = reminderTime,
    initType = reminderType,
    initSchedule = reminderSchedule,
    holderScope = holderScope
) {

    override val uiScreenState: StateFlow<EditReminderScreenState> =
        combine(
            inputHoursState,
            inputMinutesState,
            inputTypeState,
            inputScheduleState,
            canConfirmState
        ) { inputHours, inputMinutes, inputType, inputSchedule, canConfirm ->
            EditReminderScreenState(
                hours = inputHours,
                minutes = inputMinutes,
                type = inputType,
                schedule = inputSchedule,
                canConfirm = canConfirm
            )
        }.stateIn(
            holderScope,
            SharingStarted.WhileSubscribed(5000L),
            EditReminderScreenState(
                hours = inputHoursState.value,
                minutes = inputMinutesState.value,
                type = inputTypeState.value,
                schedule = inputScheduleState.value,
                canConfirm = canConfirmState.value
            )
        )

    override fun onEvent(event: EditReminderScreenEvent) {
        when (event) {
            is EditReminderScreenEvent.BaseEvent -> onBaseEvent(event.baseEvent)
        }
    }

    override fun onConfirm() {
        setUpResult(
            EditReminderScreenResult.Confirm(
                reminderId = reminderId,
                reminderTime = LocalTime(
                    hour = inputHoursState.value,
                    minute = inputMinutesState.value
                ),
                reminderType = inputTypeState.value,
                reminderSchedule = inputScheduleState.value
            )
        )
    }

    override fun onDismiss() {
        setUpResult(EditReminderScreenResult.Dismiss)
    }

}