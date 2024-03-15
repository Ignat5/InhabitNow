package com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.time

import com.example.inhabitnow.android.presentation.base.state_holder.BaseResultStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.time.components.PickTaskTimeProgressScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.time.components.PickTaskTimeProgressScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.time.components.PickTaskTimeProgressScreenState
import com.example.inhabitnow.core.type.ProgressLimitType
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalTime

class PickTaskTimeProgressStateHolder(
    initProgressContent: TaskContentModel.ProgressContent.Time,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<PickTaskTimeProgressScreenEvent, PickTaskTimeProgressScreenState, PickTaskTimeProgressScreenResult>() {

    private val inputLimitTypeState = MutableStateFlow(initProgressContent.limitType)

    private val inputLimitHoursState =
        MutableStateFlow(initProgressContent.limitTime.hour.toString())

    private val inputLimitMinutesState =
        MutableStateFlow(initProgressContent.limitTime.hour.toString())

    override val uiScreenState: StateFlow<PickTaskTimeProgressScreenState> =
        combine(
            inputLimitTypeState,
            inputLimitHoursState,
            inputLimitMinutesState
        ) { inputLimitType, inputLimitHours, inputLimitMinutes ->
            provideScreenState(
                limitType = inputLimitType,
                limitHours = inputLimitHours,
                limitMinutes = inputLimitMinutes
            )
        }.stateIn(
            holderScope,
            SharingStarted.WhileSubscribed(5000L),
            provideScreenState(
                limitType = inputLimitTypeState.value,
                limitHours = inputLimitHoursState.value,
                limitMinutes = inputLimitMinutesState.value
            )
        )

    override fun onEvent(event: PickTaskTimeProgressScreenEvent) {
        when (event) {
            is PickTaskTimeProgressScreenEvent.OnInputUpdateHours ->
                onInputUpdateHours(event)

            is PickTaskTimeProgressScreenEvent.OnInputUpdateMinutes ->
                onInputUpdateMinutes(event)

            is PickTaskTimeProgressScreenEvent.OnPickLimitType ->
                onPickLimitType(event)

            is PickTaskTimeProgressScreenEvent.OnConfirmClick ->
                onConfirmClick()

            is PickTaskTimeProgressScreenEvent.OnDismissRequest ->
                onDismissRequest()
        }
    }

    private fun onDismissRequest() {
        setUpResult(PickTaskTimeProgressScreenResult.Dismiss)
    }

    private fun onConfirmClick() {
        if (uiScreenState.value.canConfirm) {
            val limitTime = LocalTime(
                hour = inputLimitHoursState.value.toIntOrNull() ?: return,
                minute = inputLimitMinutesState.value.toIntOrNull() ?: return
            )
            setUpResult(
                PickTaskTimeProgressScreenResult.Confirm(
                    progressContent = TaskContentModel.ProgressContent.Time(
                        limitType = inputLimitTypeState.value,
                        limitTime = limitTime
                    )
                )
            )
        }
    }

    private fun onPickLimitType(event: PickTaskTimeProgressScreenEvent.OnPickLimitType) {
        inputLimitTypeState.update { event.limitType }
    }

    private fun onInputUpdateHours(event: PickTaskTimeProgressScreenEvent.OnInputUpdateHours) {
        val value = event.value
        if (value.isEmpty() || isValidHours(value)) {
            inputLimitHoursState.update { value }
        }
    }

    private fun onInputUpdateMinutes(event: PickTaskTimeProgressScreenEvent.OnInputUpdateMinutes) {
        val value = event.value
        if (value.isEmpty() || isValidMinutes(value)) {
            inputLimitMinutesState.update { value }
        }
    }

    private fun provideScreenState(
        limitType: ProgressLimitType,
        limitHours: String,
        limitMinutes: String
    ): PickTaskTimeProgressScreenState {
        return PickTaskTimeProgressScreenState(
            limitType = limitType,
            limitHours = limitHours,
            limitMinutes = limitMinutes,
            canConfirm = isValidHours(limitHours) && isValidMinutes(limitMinutes)
        )
    }

    private fun isValidHours(value: String) =
        validateDigitsCount(value) && value.toIntOrNull()
            ?.let { it in MIN_HOURS..MAX_HOURS } ?: false

    private fun isValidMinutes(value: String) =
        validateDigitsCount(value) && value.toIntOrNull()
            ?.let { it in MIN_MINUTES..MAX_MINUTES } ?: false

    private fun validateDigitsCount(value: String) = value.length <= MAX_DIGITS

    companion object {
        private const val MIN_HOURS = 0
        private const val MAX_HOURS = 23
        private const val MIN_MINUTES = 0
        private const val MAX_MINUTES = 59
        private const val MAX_DIGITS = 2
    }

}