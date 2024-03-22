package com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_description.components

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult

sealed interface PickTaskDescriptionScreenResult : ScreenResult {
    data class Confirm(val description: String) : PickTaskDescriptionScreenResult
    data object Dismiss : PickTaskDescriptionScreenResult
}