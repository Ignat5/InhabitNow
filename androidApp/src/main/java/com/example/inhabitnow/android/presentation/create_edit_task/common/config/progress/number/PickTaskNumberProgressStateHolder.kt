package com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.number

import com.example.inhabitnow.android.presentation.base.state_holder.BaseResultStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.number.components.PickTaskNumberProgressScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.number.components.PickTaskNumberProgressScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.number.components.PickTaskNumberProgressScreenState
import com.example.inhabitnow.core.type.ProgressLimitType
import com.example.inhabitnow.domain.model.task.content.TaskContentModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class PickTaskNumberProgressStateHolder(
    initProgressContent: TaskContentModel.ProgressContent.Number,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<PickTaskNumberProgressScreenEvent, PickTaskNumberProgressScreenState, PickTaskNumberProgressScreenResult>() {

    private val inputLimitTypeState =
        MutableStateFlow<ProgressLimitType>(initProgressContent.limitType)

    private val inputLimitNumberState =
        MutableStateFlow<String>(initProgressContent.limitNumber)

    private val inputLimitUnitState =
        MutableStateFlow<String>(initProgressContent.limitUnit)

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
        val value = event.value
        val isValid = value.length <= MAX_CHAR_COUNT && validateNumber(value)
        if (value.isEmpty() || isValid) {
            inputLimitNumberState.update { event.value }
        }
    }

    private fun onInputUpdateLimitUnit(event: PickTaskNumberProgressScreenEvent.OnInputUpdateLimitUnit) {
        inputLimitUnitState.update { event.value }
    }

    private fun onConfirmClick() {
        if (uiScreenState.value.canSave) {
            setUpResult(
                PickTaskNumberProgressScreenResult.Confirm(
                    progressContent = TaskContentModel.ProgressContent.Number(
                        limitType = inputLimitTypeState.value,
                        limitNumber = inputLimitNumberState.value,
                        limitUnit = inputLimitUnitState.value
                    )
                )
            )
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
            limitUnit = limitUnit,
            canSave = validateNumber(limitNumber)
        )
    }

    private fun validateNumber(limitNumber: String) =
        limitNumber.toDoubleOrNull()?.let { it in MIN_LIMIT_NUMBER..MAX_LIMIT_NUMBER } ?: false

    companion object {
        private const val MIN_LIMIT_NUMBER: Double = 0.0
        private const val MAX_LIMIT_NUMBER: Double = 1_000_000.0
        private val MAX_CHAR_COUNT = MAX_LIMIT_NUMBER.toString().count()
    }

}