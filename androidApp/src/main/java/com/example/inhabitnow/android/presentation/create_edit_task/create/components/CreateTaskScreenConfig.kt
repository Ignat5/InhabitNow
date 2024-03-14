package com.example.inhabitnow.android.presentation.create_edit_task.create.components

import com.example.inhabitnow.android.presentation.base.components.config.ScreenConfig
import com.example.inhabitnow.android.presentation.create_edit_task.common.config.pick_task_title.PickTaskTitleStateHolder

sealed interface CreateTaskScreenConfig : ScreenConfig {
    data class PickTitle(val stateHolder: PickTaskTitleStateHolder) : CreateTaskScreenConfig
}