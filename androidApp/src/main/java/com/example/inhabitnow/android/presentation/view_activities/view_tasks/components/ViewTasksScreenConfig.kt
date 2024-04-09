package com.example.inhabitnow.android.presentation.view_activities.view_tasks.components

import com.example.inhabitnow.android.presentation.base.components.config.ScreenConfig
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenConfig
import com.example.inhabitnow.core.type.TaskType

sealed interface ViewTasksScreenConfig : ScreenConfig {
    data class Base(val baseNS: BaseViewTasksScreenConfig) : ViewTasksScreenConfig
    data class PickTaskType(val allTaskTypes: List<TaskType>) : ViewTasksScreenConfig
}