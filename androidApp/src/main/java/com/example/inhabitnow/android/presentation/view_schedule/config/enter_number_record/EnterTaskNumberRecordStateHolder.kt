package com.example.inhabitnow.android.presentation.view_schedule.config.enter_number_record

import com.example.inhabitnow.android.presentation.base.state_holder.BaseResultStateHolder
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_number_record.components.EnterTaskNumberRecordScreenEvent
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_number_record.components.EnterTaskNumberRecordScreenResult
import com.example.inhabitnow.android.presentation.view_schedule.config.enter_number_record.components.EnterTaskNumberRecordScreenState
import com.example.inhabitnow.android.presentation.view_schedule.model.TaskWithRecordModel
import com.example.inhabitnow.android.ui.limitNumberToString
import com.example.inhabitnow.domain.model.record.content.RecordContentModel
import com.example.inhabitnow.domain.util.DomainConst
import com.example.inhabitnow.domain.util.DomainUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate

class EnterTaskNumberRecordStateHolder(
    private val taskWithRecord: TaskWithRecordModel.Habit.HabitContinuous.HabitNumber,
    private val date: LocalDate,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<EnterTaskNumberRecordScreenEvent, EnterTaskNumberRecordScreenState, EnterTaskNumberRecordScreenResult>() {

    private val initNumber: Double =
        (taskWithRecord.recordEntry as? RecordContentModel.Entry.Number)?.number ?: DomainConst.MIN_LIMIT_NUMBER

    private val inputNumberValidator: (String) -> Boolean = { input ->
        input.isEmpty() || DomainUtil.validateInputLimitNumber(input)
    }

    private val inputNumberState = MutableStateFlow(initNumber.limitNumberToString())

    private val canConfirmState = inputNumberState.map { input ->
        DomainUtil.validateInputLimitNumber(input)
    }.stateIn(
        holderScope,
        SharingStarted.Eagerly,
        false
    )

    override val uiScreenState: StateFlow<EnterTaskNumberRecordScreenState> =
        combine(inputNumberState, canConfirmState) { inputNumber, canConfirm ->
            EnterTaskNumberRecordScreenState(
                inputNumber = inputNumber,
                task = taskWithRecord.task,
                canConfirm = canConfirm,
                inputValidator = inputNumberValidator,
                date = date
            )
        }.stateIn(
            holderScope,
            SharingStarted.Eagerly,
            EnterTaskNumberRecordScreenState(
                inputNumber = inputNumberState.value,
                task = taskWithRecord.task,
                canConfirm = canConfirmState.value,
                inputValidator = inputNumberValidator,
                date = date
            )
        )

    override fun onEvent(event: EnterTaskNumberRecordScreenEvent) {
        when (event) {
            is EnterTaskNumberRecordScreenEvent.InputUpdateNumber ->
                onInputUpdateNumber(event)

            is EnterTaskNumberRecordScreenEvent.OnConfirmClick ->
                onConfirmClick()

            is EnterTaskNumberRecordScreenEvent.OnDismissRequest ->
                onDismissRequest()
        }
    }

    private fun onInputUpdateNumber(event: EnterTaskNumberRecordScreenEvent.InputUpdateNumber) {
        inputNumberState.update { event.value }
    }

    private fun onConfirmClick() {
        inputNumberState.value.toDoubleOrNull()?.let { number ->
            setUpResult(
                EnterTaskNumberRecordScreenResult.Confirm(
                    taskId = taskWithRecord.task.id,
                    number = number,
                    date = date
                )
            )
        }
    }

    private fun onDismissRequest() {
        setUpResult(EnterTaskNumberRecordScreenResult.Dismiss)
    }

}