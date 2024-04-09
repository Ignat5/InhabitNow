package com.example.inhabitnow.android.presentation.view_activities.view_tasks.components

import com.example.inhabitnow.android.presentation.base.components.navigation.ScreenNavigation
import com.example.inhabitnow.android.presentation.view_activities.base.components.BaseViewTasksScreenNavigation

sealed interface ViewTasksScreenNavigation : ScreenNavigation {
    data class Base(val baseNS: BaseViewTasksScreenNavigation) : ViewTasksScreenNavigation
    data class CreateTask(val taskId: String) : ViewTasksScreenNavigation
}