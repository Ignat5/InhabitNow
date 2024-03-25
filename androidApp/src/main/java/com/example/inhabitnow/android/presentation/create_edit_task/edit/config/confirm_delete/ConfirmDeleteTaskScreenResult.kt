package com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_delete

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult

sealed interface ConfirmDeleteTaskScreenResult : ScreenResult {
    data class Confirm(val taskId: String) : ConfirmDeleteTaskScreenResult
    data object Dismiss : ConfirmDeleteTaskScreenResult
}