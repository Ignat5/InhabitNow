package com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.create

import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.base.BaseCreateEditTagStateHolder
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.create.components.CreateTagScreenEvent
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.create.components.CreateTagScreenResult
import com.example.inhabitnow.android.presentation.view_tags.config.create_edit_tag.create.components.CreateTagScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class CreateTagStateHolder(
    holderScope: CoroutineScope
) : BaseCreateEditTagStateHolder<CreateTagScreenEvent, CreateTagScreenState, CreateTagScreenResult>(
    holderScope = holderScope
) {
    override val uiScreenState: StateFlow<CreateTagScreenState> =
        combine(inputTitleState, canConfirmState) { inputTitle, canConfirm ->
            CreateTagScreenState(
                inputTitle = inputTitle,
                canConfirm = canConfirm
            )
        }.stateIn(
            holderScope,
            SharingStarted.WhileSubscribed(5000L),
            CreateTagScreenState(
                inputTitle = inputTitleState.value,
                canConfirm = canConfirmState.value
            )
        )

    override fun onEvent(event: CreateTagScreenEvent) {
        when (event) {
            is CreateTagScreenEvent.BaseEvent -> onBaseEvent(event.baseEvent)
        }
    }

    override fun onConfirm() {
        setUpResult(CreateTagScreenResult.Confirm(inputTitleState.value))
    }

    override fun onDismiss() {
        setUpResult(CreateTagScreenResult.Dismiss)
    }

}