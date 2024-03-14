package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent

sealed interface PickTaskTitleScreenEvent : ScreenEvent {
    data class OnInputUpdate(val value: String) : PickTaskTitleScreenEvent
    data object OnConfirmClick : PickTaskTitleScreenEvent
    data object OnDismissRequest : PickTaskTitleScreenEvent
}