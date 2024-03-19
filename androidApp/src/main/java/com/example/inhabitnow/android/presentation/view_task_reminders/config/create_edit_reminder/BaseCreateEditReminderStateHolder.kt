package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.android.presentation.base.state_holder.BaseResultStateHolder
import com.example.inhabitnow.android.presentation.model.UIReminderContent
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.components.BaseCreateEditReminderScreenEvent
import com.example.inhabitnow.android.presentation.view_task_reminders.config.create_edit_reminder.components.BaseCreateEditReminderScreenState
import com.example.inhabitnow.core.type.ReminderType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime

abstract class BaseCreateEditReminderStateHolder<SE : ScreenEvent, SS : BaseCreateEditReminderScreenState, SR : ScreenResult>(
    time: LocalTime? = null,
    type: ReminderType? = null,
    schedule: UIReminderContent.Schedule? = null,
    final override val holderScope: CoroutineScope
) : BaseResultStateHolder<SE, SS, SR>() {

    abstract fun onConfirm()
    abstract fun onDismiss()

    protected val inputHoursState = MutableStateFlow(time?.hour ?: defaultTime.hour)
    protected val inputMinutesState = MutableStateFlow(time?.minute ?: defaultTime.minute)
    protected val inputTypeState = MutableStateFlow(type ?: defaultType)
    protected val inputScheduleState = MutableStateFlow(schedule ?: defaultSchedule)

    protected val canConfirmState = inputScheduleState.map { schedule ->
        when (schedule) {
            is UIReminderContent.Schedule.EveryDay -> true
            is UIReminderContent.Schedule.DaysOfWeek -> schedule.daysOfWeek.isNotEmpty()
        }
    }.stateIn(
        holderScope,
        SharingStarted.Eagerly,
        true
    )

    protected fun onBaseEvent(event: BaseCreateEditReminderScreenEvent) {
        when (event) {
            is BaseCreateEditReminderScreenEvent.OnHoursValueUpdate ->
                onHoursValueUpdate(event)

            is BaseCreateEditReminderScreenEvent.OnMinutesValueUpdate ->
                onMinutesValueUpdate(event)

            is BaseCreateEditReminderScreenEvent.OnReminderTypeClick ->
                onReminderTypeClick(event)

            is BaseCreateEditReminderScreenEvent.OnReminderScheduleTypeClick ->
                onReminderScheduleTypeClick(event)

            is BaseCreateEditReminderScreenEvent.OnDayOfWeekClick ->
                onDayOfWeekClick(event)

            is BaseCreateEditReminderScreenEvent.OnConfirmClick ->
                onConfirmClick()

            is BaseCreateEditReminderScreenEvent.OnDismissRequest ->
                onDismissRequest()
        }
    }

    private fun onHoursValueUpdate(event: BaseCreateEditReminderScreenEvent.OnHoursValueUpdate) {
        inputHoursState.update { event.hours }
    }

    private fun onMinutesValueUpdate(event: BaseCreateEditReminderScreenEvent.OnMinutesValueUpdate) {
        inputMinutesState.update { event.minutes }
    }

    private fun onReminderTypeClick(event: BaseCreateEditReminderScreenEvent.OnReminderTypeClick) {
        inputTypeState.update { event.type }
    }

    private fun onReminderScheduleTypeClick(event: BaseCreateEditReminderScreenEvent.OnReminderScheduleTypeClick) {
        val clickedType = event.type
        if (inputScheduleState.value.type != clickedType) {
            inputScheduleState.update {
                when (clickedType) {
                    UIReminderContent.Schedule.Type.EveryDay -> UIReminderContent.Schedule.EveryDay
                    UIReminderContent.Schedule.Type.DaysOfWeek -> UIReminderContent.Schedule.DaysOfWeek(
                        emptySet()
                    )
                }
            }
        }
    }

    private fun onDayOfWeekClick(event: BaseCreateEditReminderScreenEvent.OnDayOfWeekClick) {
        inputScheduleState.update { oldSchedule ->
            (oldSchedule as? UIReminderContent.Schedule.DaysOfWeek)?.daysOfWeek?.let { oldSet ->
                val clickedDayOfWeek = event.dayOfWeek
                val newSet = mutableSetOf<DayOfWeek>()
                newSet.addAll(oldSet)
                if (newSet.contains(clickedDayOfWeek)) newSet.remove(clickedDayOfWeek)
                else newSet.add(clickedDayOfWeek)
                UIReminderContent.Schedule.DaysOfWeek(newSet)
            } ?: oldSchedule
        }
    }

    private fun onConfirmClick() {
        if (canConfirmState.value) {
            onConfirm()
        }
    }

    private fun onDismissRequest() {
        onDismiss()
    }

    private val defaultTime: LocalTime
        get() = LocalTime(hour = 12, minute = 0)

    private val defaultType: ReminderType
        get() = ReminderType.NoReminder

    private val defaultSchedule: UIReminderContent.Schedule
        get() = UIReminderContent.Schedule.EveryDay

}