package com.example.inhabitnow.android.presentation.view_activities.base.components

import com.example.inhabitnow.android.presentation.base.components.config.ScreenConfig

sealed interface BaseViewTasksScreenConfig : ScreenConfig {

    data class ConfirmArchiveTask(
        val taskId: String
    ) : BaseViewTasksScreenConfig

    data class ConfirmDeleteTask(
        val taskId: String
    ) : BaseViewTasksScreenConfig

}