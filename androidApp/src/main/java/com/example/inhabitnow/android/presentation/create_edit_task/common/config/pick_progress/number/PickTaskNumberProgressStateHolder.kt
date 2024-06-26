package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.number

import com.example.inhabitnow.android.presentation.base.state_holder.BaseResultStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.number.components.PickTaskNumberProgressScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.number.components.PickTaskNumberProgressScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_progress.number.components.PickTaskNumberProgressScreenState
import com.example.inhabitnow.android.ui.limitNumberToString
import com.example.inhabitnow.core.type.ProgressLimitType
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import com.example.inhabitnow.domain.use_case.validate_limit_number.ValidateInputLimitNumberUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class PickTaskNumberProgressStateHolder(
    initProgressContent: TaskContentModel.ProgressContent.Number,
    private val validateInputLimitNumberUseCase: ValidateInputLimitNumberUseCase,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<PickTaskNumberProgressScreenEvent, PickTaskNumberProgressScreenState, PickTaskNumberProgressScreenResult>() {

    private val inputLimitTypeState =
        MutableStateFlow<ProgressLimitType>(initProgressContent.limitType)

    private val inputLimitNumberState =
        MutableStateFlow<String>(initProgressContent.limitNumber.limitNumberToString())

    private val inputLimitUnitState =
        MutableStateFlow<String>(initProgressContent.limitUnit)

    private val limitNumberValidator: (value: String) -> Boolean = { value ->
        value.isEmpty() || validateInputLimitNumberUseCase(value)
    }

    override val uiScreenState: StateFlow<PickTaskNumberProgressScreenState> =
        combine(
            inputLimitTypeState,
            inputLimitNumberState,
            inputLimitUnitState
        ) { inputLimitType, inputLimitNumber, inputLimitUnit ->
            provideScreenState(
                limitType = inputLimitType,
                limitNumber = inputLimitNumber,
                limitUnit = inputLimitUnit
            )
        }.stateIn(
            holderScope,
            SharingStarted.WhileSubscribed(5000L),
            provideScreenState(
                limitType = inputLimitTypeState.value,
                limitNumber = inputLimitNumberState.value,
                limitUnit = inputLimitUnitState.value
            )
        )

    override fun onEvent(event: PickTaskNumberProgressScreenEvent) {
        when (event) {
            is PickTaskNumberProgressScreenEvent.OnInputUpdateLimitNumber ->
                onInputUpdateLimitNumber(event)

            is PickTaskNumberProgressScreenEvent.OnInputUpdateLimitUnit ->
                onInputUpdateLimitUnit(event)

            is PickTaskNumberProgressScreenEvent.OnPickLimitType ->
                onPickLimitType(event)

            is PickTaskNumberProgressScreenEvent.OnConfirmClick ->
                onConfirmClick()

            is PickTaskNumberProgressScreenEvent.OnDismissRequest ->
                onDismissRequest()

        }
    }

    private fun onPickLimitType(event: PickTaskNumberProgressScreenEvent.OnPickLimitType) {
        inputLimitTypeState.update { event.limitType }
    }

    private fun onInputUpdateLimitNumber(event: PickTaskNumberProgressScreenEvent.OnInputUpdateLimitNumber) {
        inputLimitNumberState.update { event.value }
    }

    private fun onInputUpdateLimitUnit(event: PickTaskNumberProgressScreenEvent.OnInputUpdateLimitUnit) {
        inputLimitUnitState.update { event.value }
    }

    private fun onConfirmClick() {
        if (uiScreenState.value.canSave) {
            inputLimitNumberState.value.toDoubleOrNull()?.let { limitNumber ->
                setUpResult(
                    PickTaskNumberProgressScreenResult.Confirm(
                        progressContent = TaskContentModel.ProgressContent.Number(
                            limitType = inputLimitTypeState.value,
                            limitNumber = limitNumber,
                            limitUnit = inputLimitUnitState.value
                        )
                    )
                )
            }
        }
    }

    private fun onDismissRequest() {
        setUpResult(PickTaskNumberProgressScreenResult.Dismiss)
    }

    private fun provideScreenState(
        limitType: ProgressLimitType,
        limitNumber: String,
        limitUnit: String
    ): PickTaskNumberProgressScreenState {
        return PickTaskNumberProgressScreenState(
            limitType = limitType,
            limitNumber = limitNumber,
            limitNumberValidator = limitNumberValidator,
            limitUnit = limitUnit,
            canSave = validateInputLimitNumberUseCase(limitNumber)
        )
    }
}