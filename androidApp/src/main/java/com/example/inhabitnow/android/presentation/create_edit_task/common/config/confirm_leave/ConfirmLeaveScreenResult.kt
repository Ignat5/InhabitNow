package com.example.inhabitnow.android.presentation.create_edit_task.common.config.confirm_leave

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult

sealed interface ConfirmLeaveScreenResult : ScreenResult {
    data object Confirm : ConfirmLeaveScreenResult
    data object Dismiss : ConfirmLeaveScreenResult
}