package com.example.inhabitnow.android.presentation.view_task_reminders.config.permission

import com.example.inhabitnow.android.presentation.base.components.result.ScreenResult

sealed interface CheckNotificationPermissionScreenResult : ScreenResult {
    data object ShowRationale : CheckNotificationPermissionScreenResult
    data object Granted : CheckNotificationPermissionScreenResult
    data object Denied : CheckNotificationPermissionScreenResult
}