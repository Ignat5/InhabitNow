package com.example.inhabitnow.android.presentation.main.components

import com.example.inhabitnow.android.presentation.base.components.config.ScreenConfig
import com.example.inhabitnow.core.type.TaskProgressType
import com.example.inhabitnow.core.type.TaskType
import kotlin.enums.EnumEntries

sealed interface MainScreenConfig : ScreenConfig {
    data class PickTaskType(val allTaskTypes: List<TaskType>) : MainScreenConfig
    data class PickTaskProgressType(val allTaskProgressTypes: List<TaskProgressType>) :
        MainScreenConfig
}