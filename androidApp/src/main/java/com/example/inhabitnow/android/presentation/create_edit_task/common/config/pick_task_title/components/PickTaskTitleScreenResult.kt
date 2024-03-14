package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.components

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult

sealed interface PickTaskTitleScreenResult : ScreenResult {
    data class Confirm(val input: String) : PickTaskTitleScreenResult
    data object Dismiss : PickTaskTitleScreenResult
}