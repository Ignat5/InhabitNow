package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_priority.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent

sealed interface PickTaskPriorityScreenEvent : ScreenEvent {
    data class OnInputUpdatePriority(
        val value: String
    ) : PickTaskPriorityScreenEvent

    data object OnConfirmClick : PickTaskPriorityScreenEvent
    data object OnDismissRequest : PickTaskPriorityScreenEvent
}