package com.example.inhabitnow.android.presentation.view_task_reminders.config.create_reminder.config.pick_time

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult

sealed interface PickTimeScreenResult : ScreenResult {
    data class Confirm(
        val hours: Int,
        val minutes: Int
    ) : PickTimeScreenResult
    data object Dismiss : PickTimeScreenResult
}