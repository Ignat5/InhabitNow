package com.example.inhabitnow.android.presentation.create_edit_task.edit.config.confirm_restart

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult

sealed interface ConfirmRestartHabitScreenResult : ScreenResult {
    data object Confirm : ConfirmRestartHabitScreenResult
    data object Dismiss : ConfirmRestartHabitScreenResult
}