package com.example.inhabitnow.android.presentation.view_activities.view_tasks.components

import com.example.inhabitnow.android.presentation.base.components.config.ScreenConfig
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenConfig

sealed interface ViewTasksScreenConfig : ScreenConfig {
    data class Base(val baseNS: BaseViewTasksScreenConfig) : ViewTasksScreenConfig
}