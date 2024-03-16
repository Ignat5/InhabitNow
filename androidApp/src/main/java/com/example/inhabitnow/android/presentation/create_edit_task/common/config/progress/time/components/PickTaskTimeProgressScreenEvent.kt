package com.example.inhabitnow.android.presentation.create_edit_task.common.config.progress.time.components

import com.example.inhabitnow.android.presentation.base.components.event.ScreenEvent
import com.example.inhabitnow.core.type.ProgressLimitType

sealed interface PickTaskTimeProgressScreenEvent : ScreenEvent {
    data class OnPickLimitType(val limitType: ProgressLimitType) : PickTaskTimeProgressScreenEvent
    data class OnInputUpdateHours(val value: Int) : PickTaskTimeProgressScreenEvent
    data class OnInputUpdateMinutes(val value: Int) : PickTaskTimeProgressScreenEvent

    data object OnConfirmClick : PickTaskTimeProgressScreenEvent
    data object OnDismissRequest : PickTaskTimeProgressScreenEvent
}