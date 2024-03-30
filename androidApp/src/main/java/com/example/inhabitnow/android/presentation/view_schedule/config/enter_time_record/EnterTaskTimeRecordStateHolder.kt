package com.example.inhabitnow.android.presentation.view_schedule.config.enter_time_record

import com.example.inhabitnow.android.presentation.base.state_holder.BaseResultStateHolder
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_time_record.components.EnterTaskTimeRecordScreenEvent
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_time_record.components.EnterTaskTimeRecordScreenResult
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_time_record.components.EnterTaskTimeRecordScreenState
import com.example.inhabitnow.android.presentation.view_schedule.model.TaskWithRecordModel
import com.example.inhabitnow.domain.model.record.content.RecordContentModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class EnterTaskTimeRecordStateHolder(
    private val taskWithRecordModel: TaskWithRecordModel.Habit.HabitContinuous.HabitTime,
    private val date: LocalDate,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<EnterTaskTimeRecordScreenEvent, EnterTaskTimeRecordScreenState, EnterTaskTimeRecordScreenResult>() {

    private val initTime =
        (taskWithRecordModel.recordEntry as? RecordContentModel.Entry.Time)?.time ?: defaultTime

    private val inputHoursState = MutableStateFlow(initTime.hour)
    private val inputMinutesState = MutableStateFlow(initTime.minute)

    override val uiScreenState: StateFlow<EnterTaskTimeRecordScreenState> =
        combine(inputHoursState, inputMinutesState) { inputHours, inputMinutes ->
            EnterTaskTimeRecordScreenState(
                inputHours = inputHours,
                inputMinutes = inputMinutes,
                task = taskWithRecordModel.task,
                date = date
            )
        }.stateIn(
            holderScope,
            SharingStarted.Eagerly,
            EnterTaskTimeRecordScreenState(
                inputHours = inputHoursState.value,
                inputMinutes = inputMinutesState.value,
                task = taskWithRecordModel.task,
                date = date
            )
        )

    override fun onEvent(event: EnterTaskTimeRecordScreenEvent) {
        when (event) {
            is EnterTaskTimeRecordScreenEvent.OnInputUpdateHours ->
                onInputUpdateHours(event)

            is EnterTaskTimeRecordScreenEvent.OnInputUpdateMinutes ->
                onInputUpdateMinutes(event)

            is EnterTaskTimeRecordScreenEvent.OnConfirmClick ->
                onConfirmClick()

            is EnterTaskTimeRecordScreenEvent.OnDismissRequest ->
                onDismissRequest()
        }
    }

    private fun onInputUpdateHours(event: EnterTaskTimeRecordScreenEvent.OnInputUpdateHours) {
        inputHoursState.update { event.hours }
    }

    private fun onInputUpdateMinutes(event: EnterTaskTimeRecordScreenEvent.OnInputUpdateMinutes) {
        inputMinutesState.update { event.minutes }
    }

    private fun onConfirmClick() {
        setUpResult(
            EnterTaskTimeRecordScreenResult.Confirm(
                taskId = taskWithRecordModel.task.id,
                date = date,
                time = LocalTime(hour = inputHoursState.value, minute = inputMinutesState.value)
            )
        )
    }

    private fun onDismissRequest() {
        setUpResult(EnterTaskTimeRecordScreenResult.Dismiss)
    }

    private val defaultTime: LocalTime
        get() = LocalTime(hour = 0, minute = 0)
}