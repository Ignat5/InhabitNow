package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_description

import com.example.inhabitnow.android.presentation.base.state_holder.BaseResultStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_description.components.PickTaskDescriptionScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_description.components.PickTaskDescriptionScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_description.components.PickTaskDescriptionScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class PickTaskDescriptionStateHolder(
    initDescription: String,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<PickTaskDescriptionScreenEvent, PickTaskDescriptionScreenState, PickTaskDescriptionScreenResult>() {

    private val inputDescriptionState = MutableStateFlow(initDescription)

    override val uiScreenState: StateFlow<PickTaskDescriptionScreenState> =
        inputDescriptionState.map { inputDescription ->
            PickTaskDescriptionScreenState(
                inputDescription = inputDescription
            )
        }.stateIn(
            holderScope,
            SharingStarted.WhileSubscribed(5000L),
            PickTaskDescriptionScreenState(
                inputDescription = inputDescriptionState.value
            )
        )

    override fun onEvent(event: PickTaskDescriptionScreenEvent) {
        when (event) {
            is PickTaskDescriptionScreenEvent.OnInputUpdateDescription ->
                onInputUpdateDescription(event)

            is PickTaskDescriptionScreenEvent.OnConfirmClick -> onConfirmClick()
            is PickTaskDescriptionScreenEvent.OnDismissRequest -> onDismissRequest()
        }
    }

    private fun onInputUpdateDescription(event: PickTaskDescriptionScreenEvent.OnInputUpdateDescription) {
        inputDescriptionState.update { event.value }
    }

    private fun onConfirmClick() {
        setUpResult(PickTaskDescriptionScreenResult.Confirm(inputDescriptionState.value))
    }

    private fun onDismissRequest() {
        setUpResult(PickTaskDescriptionScreenResult.Dismiss)
    }

}