package com.example.inhabitnow.android.presentation.main.config.pick_task_progress_type

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult
import com.example.inhabitnow.core.type.TaskProgressType

sealed interface PickTaskProgressTypeScreenResult : ScreenResult {
    data object Dismiss : PickTaskProgressTypeScreenResult
    data class Confirm(val taskProgressType: TaskProgressType) : PickTaskProgressTypeScreenResult
}