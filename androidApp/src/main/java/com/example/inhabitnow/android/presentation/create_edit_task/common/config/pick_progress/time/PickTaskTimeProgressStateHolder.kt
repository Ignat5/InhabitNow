package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.time

import com.example.inhabitnow.android.presentation.base.state_holder.BaseResultStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.time.components.PickTaskTimeProgressScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.time.components.PickTaskTimeProgressScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.time.components.PickTaskTimeProgressScreenState
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
        MutableStateFlow(initProgressContent.limitTime.hour)

    private val inputLimitMinutesState =
        MutableStateFlow(initProgressContent.limitTime.minute)

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
        setUpResult(
            PickTaskTimeProgressScreenResult.Confirm(
                progressContent = TaskContentModel.ProgressContent.Time(
                    limitType = inputLimitTypeState.value,
                    limitTime = LocalTime(
                        hour = inputLimitHoursState.value,
                        minute = inputLimitMinutesState.value
                    )
                )
            )
        )
    }

    private fun onPickLimitType(event: PickTaskTimeProgressScreenEvent.OnPickLimitType) {
        inputLimitTypeState.update { event.limitType }
    }

    private fun onInputUpdateHours(event: PickTaskTimeProgressScreenEvent.OnInputUpdateHours) {
        inputLimitHoursState.update { event.value }
    }

    private fun onInputUpdateMinutes(event: PickTaskTimeProgressScreenEvent.OnInputUpdateMinutes) {
        inputLimitMinutesState.update { event.value }
    }

    private fun provideScreenState(
        limitType: ProgressLimitType,
        limitHours: Int,
        limitMinutes: Int
    ): PickTaskTimeProgressScreenState {
        return PickTaskTimeProgressScreenState(
            limitType = limitType,
            limitHours = limitHours,
            limitMinutes = limitMinutes,
        )
    }

}