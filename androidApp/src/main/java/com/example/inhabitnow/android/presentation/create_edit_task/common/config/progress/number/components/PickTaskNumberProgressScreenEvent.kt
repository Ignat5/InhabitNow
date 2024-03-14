package com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.number.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.core.type.ProgressLimitType

sealed interface PickTaskNumberProgressScreenEvent : ScreenEvent {
    data class OnPickLimitType(val limitType: ProgressLimitType) : PickTaskNumberProgressScreenEvent
    data class OnInputUpdateLimitNumber(val value: String) : PickTaskNumberProgressScreenEvent
    data class OnInputUpdateLimitUnit(val value: String) : PickTaskNumberProgressScreenEvent

    data object OnConfirmClick : PickTaskNumberProgressScreenEvent
    data object OnDismissRequest : PickTaskNumberProgressScreenEvent
}