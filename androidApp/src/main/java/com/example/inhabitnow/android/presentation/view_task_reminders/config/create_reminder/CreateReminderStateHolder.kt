package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder

import com.example.inhabitnow.android.presentation.base.components.config.BaseConfigState
import com.example.inhabitnow.android.presentation.base.state_holder.BaseResultStateHolder
import com.example.inhabitnow.android.presentation.model.UIReminderContent
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.components.CreateReminderScreenEvent
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.components.CreateReminderScreenResult
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.components.CreateReminderScreenState
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.config.CreateReminderScreenConfig
import com.example.inhabitnow.core.type.ReminderType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime

class CreateReminderStateHolder(
    time: LocalTime? = null,
    type: ReminderType? = null,
    schedule: UIReminderContent.Schedule? = null,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<CreateReminderScreenEvent, CreateReminderScreenState, CreateReminderScreenResult>() {

    private val inputHoursState = MutableStateFlow(time?.hour ?: defaultTime.hour)
    private val inputMinutesState = MutableStateFlow(time?.minute ?: defaultTime.minute)
    private val inputTypeState = MutableStateFlow(type ?: defaultType)
    private val inputScheduleState = MutableStateFlow(schedule ?: defaultSchedule)

    override val uiScreenState: StateFlow<CreateReminderScreenState> =
        combine(
            inputHoursState, inputMinutesState, inputTypeState, inputScheduleState
        ) { inputHours, inputMinutes, inputType, inputSchedule ->
            CreateReminderScreenState(
                hours = inputHours,
                minutes = inputMinutes,
                type = inputType,
                schedule = inputSchedule,
                canConfirm = checkIfCanConfirm(inputSchedule)
            )
        }.stateIn(
            holderScope,
            SharingStarted.WhileSubscribed(5000L),
            CreateReminderScreenState(
                hours = inputHoursState.value,
                minutes = inputMinutesState.value,
                type = inputTypeState.value,
                schedule = inputScheduleState.value,
                canConfirm = checkIfCanConfirm(inputScheduleState.value)
            )
        )

    override fun onEvent(event: CreateReminderScreenEvent) {
        when (event) {
            is CreateReminderScreenEvent.OnHoursValueUpdate ->
                onHoursValueUpdate(event)

            is CreateReminderScreenEvent.OnMinutesValueUpdate ->
                onMinutesValueUpdate(event)

            is CreateReminderScreenEvent.OnPickReminderType ->
                onPickReminderType(event)

            is CreateReminderScreenEvent.OnReminderScheduleTypeClick ->
                onReminderScheduleTypeClick(event)

            is CreateReminderScreenEvent.OnDayOfWeekClick -> onDayOfWeekClick(event)
            is CreateReminderScreenEvent.OnConfirmClick -> onConfirmClick()
            is CreateReminderScreenEvent.OnDismissRequest -> onDismissRequest()
        }
    }

    private fun onHoursValueUpdate(event: CreateReminderScreenEvent.OnHoursValueUpdate) {
        inputHoursState.update { event.hours }
    }

    private fun onMinutesValueUpdate(event: CreateReminderScreenEvent.OnMinutesValueUpdate) {
        inputMinutesState.update { event.minutes }
    }

    private fun onPickReminderType(event: CreateReminderScreenEvent.OnPickReminderType) {
        inputTypeState.update { event.type }
    }

    private fun onReminderScheduleTypeClick(event: CreateReminderScreenEvent.OnReminderScheduleTypeClick) {
        val clickedType = event.type
        if (inputScheduleState.value.type != clickedType) {
            inputScheduleState.update {
                when (clickedType) {
                    UIReminderContent.Schedule.Type.EveryDay -> {
                        UIReminderContent.Schedule.EveryDay
                    }

                    UIReminderContent.Schedule.Type.DaysOfWeek -> {
                        UIReminderContent.Schedule.DaysOfWeek(emptySet())
                    }
                }
            }
        }
    }

    private fun onDayOfWeekClick(event: CreateReminderScreenEvent.OnDayOfWeekClick) {
        (inputScheduleState.value as? UIReminderContent.Schedule.DaysOfWeek)?.let { prev ->
            val oldSet = prev.daysOfWeek
            val clickedDayOfWeek = event.dayOfWeek
            inputScheduleState.update {
                val newSet = mutableSetOf<DayOfWeek>()
                newSet.addAll(oldSet)
                if (newSet.contains(clickedDayOfWeek)) newSet.remove(clickedDayOfWeek)
                else newSet.add(clickedDayOfWeek)
                UIReminderContent.Schedule.DaysOfWeek(newSet)
            }
        }
    }

    private fun onConfirmClick() {
        if (uiScreenState.value.canConfirm) {
            setUpResult(
                CreateReminderScreenResult.Confirm(
                    time = LocalTime(
                        hour = inputHoursState.value,
                        minute = inputMinutesState.value
                    ),
                    type = inputTypeState.value,
                    uiScheduleContent = inputScheduleState.value
                )
            )
        }
    }

    private fun onDismissRequest() {
        setUpResult(CreateReminderScreenResult.Dismiss)
    }

    private fun checkIfCanConfirm(schedule: UIReminderContent.Schedule) =
        when (schedule) {
            is UIReminderContent.Schedule.EveryDay -> true
            is UIReminderContent.Schedule.DaysOfWeek -> schedule.daysOfWeek.isNotEmpty()
        }

    private val defaultTime: LocalTime
        get() = LocalTime(hour = 12, minute = 0)

    private val defaultType: ReminderType
        get() = ReminderType.NoReminder

    private val defaultSchedule: UIReminderContent.Schedule
        get() = UIReminderContent.Schedule.EveryDay

}