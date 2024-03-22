package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_description.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent

sealed interface PickTaskDescriptionScreenEvent : ScreenEvent {
    data class OnInputUpdateDescription(val value: String) : PickTaskDescriptionScreenEvent
    data object OnConfirmClick : PickTaskDescriptionScreenEvent
    data object OnDismissRequest : PickTaskDescriptionScreenEvent
}