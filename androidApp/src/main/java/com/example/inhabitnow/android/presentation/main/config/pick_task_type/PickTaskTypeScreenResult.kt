package com.example.inhabitnow.android.presentation.main.config.pick_task_type

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.core.type.TaskType

sealed interface PickTaskTypeScreenResult : ScreenResult {
    data object Dismiss : PickTaskTypeScreenResult
    data class Confirm(val taskType: TaskType) : PickTaskTypeScreenResult
}