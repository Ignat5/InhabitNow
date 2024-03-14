package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title

import com.example.inhabitnow.android.presentation.base.state_holder.BaseResultStateHolder
import com.example.inhabitnow.android.presentation.base.state_holder.BaseStateHolder
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.components.PickTaskTitleScreenEvent
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.components.PickTaskTitleScreenResult
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.components.PickTaskTitleScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class PickTaskTitleStateHolder(
    initTitle: String,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<PickTaskTitleScreenEvent, PickTaskTitleScreenState, PickTaskTitleScreenResult>(
) {

    private val inputTitleState = MutableStateFlow(initTitle)

    override val uiScreenState: StateFlow<PickTaskTitleScreenState> =
        inputTitleState.map { inputTitle ->
            provideState(inputTitle)
        }.stateIn(
            holderScope,
            SharingStarted.WhileSubscribed(5000L),
            provideState(inputTitleState.value)
        )

    override fun onEvent(event: PickTaskTitleScreenEvent) {
        when (event) {
            is PickTaskTitleScreenEvent.OnInputUpdate -> onInputUpdate(event)
            is PickTaskTitleScreenEvent.OnConfirmClick -> onConfirmClick()
            is PickTaskTitleScreenEvent.OnDismissRequest -> onDismissRequest()
        }
    }

    private fun onInputUpdate(event: PickTaskTitleScreenEvent.OnInputUpdate) {
        inputTitleState.update { event.value }
    }

    private fun onConfirmClick() {
        if (uiScreenState.value.canConfirm) {
            setUpResult(PickTaskTitleScreenResult.Confirm(inputTitleState.value))
        }
    }

    private fun onDismissRequest() {
        setUpResult(PickTaskTitleScreenResult.Dismiss)
    }

    private fun provideState(inputTitle: String): PickTaskTitleScreenState {
        return PickTaskTitleScreenState(
            inputTitle = inputTitle,
            canConfirm = inputTitle.isNotBlank()
        )
    }

}