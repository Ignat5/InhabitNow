package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_priority

import com.example.inhabitnow.android.presentation.base.state_holder.BaseResultStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_priority.components.PickTaskPriorityScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_priority.components.PickTaskPriorityScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_priority.components.PickTaskPriorityScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class PickTaskPriorityStateHolder(
    initPriority: String,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<PickTaskPriorityScreenEvent, PickTaskPriorityScreenState, PickTaskPriorityScreenResult>() {

    private val inputPriorityState = MutableStateFlow(initPriority)

    private val valueValidator: (String) -> Boolean = { input ->
        input.isEmpty() || validate(input)
    }

    private val canConfirmState = inputPriorityState.map { input ->
        validate(input)
    }.stateIn(
        holderScope,
        SharingStarted.Eagerly,
        false
    )

    override val uiScreenState: StateFlow<PickTaskPriorityScreenState> =
        combine(inputPriorityState, canConfirmState) { inputPriority, canConfirm ->
            PickTaskPriorityScreenState(
                inputPriority = inputPriority,
                canConfirm = canConfirm,
                valueValidator = valueValidator
            )
        }.stateIn(
            holderScope,
            SharingStarted.WhileSubscribed(5000L),
            PickTaskPriorityScreenState(
                inputPriority = inputPriorityState.value,
                canConfirm = canConfirmState.value,
                valueValidator = valueValidator
            )
        )

    override fun onEvent(event: PickTaskPriorityScreenEvent) {
        when (event) {
            is PickTaskPriorityScreenEvent.OnInputUpdatePriority ->
                onInputUpdatePriority(event)

            is PickTaskPriorityScreenEvent.OnConfirmClick -> onConfirmClick()
            is PickTaskPriorityScreenEvent.OnDismissRequest -> onDismissRequest()
        }
    }

    private fun onInputUpdatePriority(event: PickTaskPriorityScreenEvent.OnInputUpdatePriority) {
        inputPriorityState.update { event.value }
    }

    private fun onConfirmClick() {
        if (canConfirmState.value) {
            inputPriorityState.value.toIntOrNull()?.toString()?.let { priority ->
                setUpResult(PickTaskPriorityScreenResult.Confirm(priority))
            }
        }
    }

    private fun onDismissRequest() {
        setUpResult(PickTaskPriorityScreenResult.Dismiss)
    }

    private fun validate(inputPriority: String): Boolean = inputPriority.toIntOrNull()?.let {
        it in MIN_PRIORITY..MAX_PRIORITY && inputPriority.length <= MAX_LENGTH
    } ?: false

    companion object {
        private const val MIN_PRIORITY = 0
        private const val MAX_PRIORITY = 999
        private const val MAX_LENGTH = MAX_PRIORITY.toString().length
    }

}