package com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_archive

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult

sealed interface ConfirmArchiveTaskScreenResult : ScreenResult {
    data class Confirm(val taskId: String) : ConfirmArchiveTaskScreenResult
    data object Dismiss : ConfirmArchiveTaskScreenResult
}