package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_priority.components

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult

sealed interface PickTaskPriorityScreenResult : ScreenResult {
    data class Confirm(val priority: Int) : PickTaskPriorityScreenResult
    data object Dismiss : PickTaskPriorityScreenResult
}