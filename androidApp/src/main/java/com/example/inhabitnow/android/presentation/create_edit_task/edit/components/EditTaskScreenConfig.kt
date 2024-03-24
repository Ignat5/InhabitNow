package com.example.inhabitnow.android.presentation.create_edit_task.edit.components

import com.example.inhabitnow.android.presentation.base.components.config.ScreenConfig
import com.example.inhabitnow.android.presentation.create_edit_task.base.components.BaseCreateEditTaskScreenConfig

sealed interface EditTaskScreenConfig : ScreenConfig {
    data class BaseConfig(val baseConfig: BaseCreateEditTaskScreenConfig) : EditTaskScreenConfig

    data object ConfirmArchiveTask : EditTaskScreenConfig
    data object ConfirmDeleteTask : EditTaskScreenConfig
    data object ConfirmRestartHabit : EditTaskScreenConfig
}